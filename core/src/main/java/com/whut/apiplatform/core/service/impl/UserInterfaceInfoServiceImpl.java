package com.whut.apiplatform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.apiplatform.core.mapper.UserInterfaceInfoMapper;
import com.whut.apiplatform.model.entity.UserInterfaceInfo;
import com.whut.apiplatform.service.UserInterfaceInfoService;
import com.whut.webs.exception.BusinessException;
import com.whut.webs.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.whut.apiplatform.constant.UserInterfaceInfoConstant.*;

/**
 * @author laowang
 * @description 针对表【user_interface_info(用户接口表)】的数据库操作Service实现
 * @createDate 2024-08-24 20:59:41
 */
@DubboService
@AllArgsConstructor
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {


    private final StringRedisTemplate redisTemplate;


    private final RedissonClient redissonClient;


    private final ExecutorService UPDATE_POOL = Executors.newCachedThreadPool();


    private final ConcurrentSkipListMap<Long, Integer> idLeftNumberMap = new ConcurrentSkipListMap<>();


    private final ConcurrentHashMap<Long, Lock> lockMap = new ConcurrentHashMap<>();

    /**
     * 检查最小剩余数量是否满足要求
     *
     * @param interfaceInfoId 接口信息ID
     * @return 如果最小剩余数量满足要求，则返回true；否则返回false
     * @throws BusinessException 当操作异常时抛出该异常
     */
    @Override
    public Boolean checkLeast(String interfaceInfoId) {
        // check whether cache is exist
        final String cacheKey = LEFT_NUMBER_KEY + interfaceInfoId;
        boolean hasKey = redisTemplate.hasKey(cacheKey);

        final Long id = Long.valueOf(interfaceInfoId);
        if (hasKey)
            return decreaseAndUpdateLeft(cacheKey, id);


        // else lock and select from MySQL
        final String lockKey = LEFT_LOCK_KEY + interfaceInfoId;
        final RLock lock = redissonClient.getLock(lockKey);
        boolean locked = lock.tryLock();

        if (locked) {
            try {
                hasKey = redisTemplate.hasKey(cacheKey);
                if (hasKey)
                    return decreaseAndUpdateLeft(cacheKey, id);

                final long storeNum = this.baseMapper.getLeftNum(Long.valueOf(interfaceInfoId)) - 1;

                UPDATE_POOL.submit(() ->
                        redisTemplate.opsForValue().set(cacheKey, String.valueOf(storeNum), LEFT_TTL, TimeUnit.HOURS));

                if (storeNum < 0)
                    return false;

                idLeftNumberMap.put(id, (int) storeNum);
                return true;


            } catch (Exception e) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            } finally {
                lock.unlock();
            }
        }

        while (true) {
            hasKey = redisTemplate.hasKey(cacheKey);
            if (hasKey)
                return decreaseAndUpdateLeft(cacheKey, id);

            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 减少并更新Redis缓存中指定键的值，并更新内存映射中对应id的剩余数量
     *
     * @param cacheKey Redis缓存中键的名称
     * @param id       对应的id
     * @return         如果更新成功，返回true；否则返回false
     * @throws RuntimeException 如果加锁失败或更新内存映射时发生异常，则抛出运行时异常
     */
    boolean decreaseAndUpdateLeft(String cacheKey, Long id) {
        Long resultNum = redisTemplate.opsForValue().decrement(cacheKey);

        UPDATE_POOL.submit(() -> redisTemplate.expire(cacheKey, LEFT_TTL, TimeUnit.HOURS));

        if (resultNum < 0)
            return false;

        final Lock lock = lockMap.computeIfAbsent(id, k -> new ReentrantLock());
        while (true) {
            final boolean locked;
            try {
                locked = lock.tryLock(3000L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (!locked)
                continue;

            try {
                final Integer leftNum = idLeftNumberMap.get(id);
                if (leftNum == null || resultNum < leftNum)
                    idLeftNumberMap.put(id, Math.toIntExact(resultNum));
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }

    }


    /**
     * 定时更新数据库中最小剩余数量
     *
     * @Scheduled 注解表示这是一个定时任务，固定频率执行，初始延迟一定时间
     * fixedRate = 50L 表示每隔50毫秒执行一次
     * initialDelay = 1000L 表示任务启动后延迟1000毫秒执行第一次
     */
    @Scheduled(fixedRate = 50L, initialDelay = 1000L)
    protected void updateLeastInDB() {
        Map.Entry<Long, Integer> firstEntry = idLeftNumberMap.firstEntry();
        if (firstEntry == null)
            return;

        final Long id = firstEntry.getKey();
        final Lock lock = lockMap.computeIfAbsent(id, k -> new ReentrantLock());

        Integer leftNum;
        while (true) {
            final boolean locked;
            try {
                locked = lock.tryLock(3000L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (!locked)
                continue;

            try {
                leftNum = idLeftNumberMap.get(id);
                idLeftNumberMap.remove(id);
                break;
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }

        }


        LambdaUpdateWrapper<UserInterfaceInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserInterfaceInfo::getInterfaceInfoId, id);

        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        userInterfaceInfo.setLeftNum(leftNum);

        boolean updated = this.update(userInterfaceInfo, wrapper);


        // todo check whether it updated successfully
    }
}




