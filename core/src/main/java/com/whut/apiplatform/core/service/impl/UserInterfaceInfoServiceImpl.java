package com.whut.apiplatform.core.service.impl;

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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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


    private final static ExecutorService UPDATE_CACHE_POOL = Executors.newCachedThreadPool();


    @Override
    public Boolean checkLeast(String interfaceInfoId) {
        // check whether cache is exist
        final String cacheKey = LEFT_NUMBER_KEY + interfaceInfoId;
        boolean hasKey = redisTemplate.hasKey(cacheKey);

        if (hasKey)
            return decreaseAndUpdateLeft(cacheKey);


        // else lock and select from MySQL
        final String lockKey = LEFT_LOCK_KEY + interfaceInfoId;
        final RLock lock = redissonClient.getLock(lockKey);
        boolean locked = lock.tryLock();

        if (locked) {
            try {
                hasKey = redisTemplate.hasKey(cacheKey);
                if (hasKey)
                    return decreaseAndUpdateLeft(cacheKey);

                final long storeNum = this.baseMapper.getLeftNum(Long.valueOf(interfaceInfoId)) - 1;

                UPDATE_CACHE_POOL.submit(() ->
                        redisTemplate.opsForValue().set(cacheKey, String.valueOf(storeNum), LEFT_TTL, TimeUnit.HOURS));

                return storeNum >= 0;

            } catch (Exception e) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            } finally {
                lock.unlock();
            }
        }

        while (true) {
            hasKey = redisTemplate.hasKey(cacheKey);
            if (hasKey)
                return decreaseAndUpdateLeft(cacheKey);

            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    boolean decreaseAndUpdateLeft(String cacheKey) {
        Long resultNum = redisTemplate.opsForValue().decrement(cacheKey);

        UPDATE_CACHE_POOL.submit(() -> redisTemplate.expire(cacheKey, LEFT_TTL, TimeUnit.HOURS));

        return resultNum >= 0;
    }
}




