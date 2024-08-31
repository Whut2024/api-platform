package com.whut.apiplatform.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.apiplatform.constant.UserConstant;
import com.whut.apiplatform.core.mapper.UserMapper;
import com.whut.apiplatform.model.dto.user.TokenUser;
import com.whut.apiplatform.model.dto.user.UserLoginRequest;
import com.whut.apiplatform.model.dto.user.UserRegisterRequest;
import com.whut.apiplatform.model.dto.user.VersionUser;
import com.whut.apiplatform.model.entity.User;
import com.whut.apiplatform.service.UserService;
import com.whut.starter.exception.BusinessException;
import com.whut.starter.exception.ErrorCode;
import com.whut.starter.exception.ThrowUtils;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.whut.apiplatform.constant.UserConstant.*;

/**
* @author laowang
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-08-24 20:59:37
*/
@DubboService
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {


    private final StringRedisTemplate redisTemplate;


    private final RedissonClient redissonClient;


    @Override
    public User login(UserLoginRequest userLoginRequest) {
        final String userAccount = userLoginRequest.getUserAccount();
        final String userPassword = SecureUtil.md5(userLoginRequest.getUserPassword());

        // select table
        final LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserAccount, userAccount).eq(User::getUserPassword, userPassword);

        final User user = this.getOne(wrapper);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR, "account or password do not exist");


        RLock lock = redissonClient.getLock(USER_LOGIN_LOCK_KEY + user.getId());

        try {
            boolean locked = lock.tryLock(0L, USER_LOGIN_LOCK_TTL, TimeUnit.SECONDS);


            if (locked) {
                // check login version
                final String versionKey = USER_LOGIN_VERSION_KEY + user.getId();
                final Long version = redisTemplate.opsForValue().increment(versionKey);


                // store cache in redis
                final String token = SecureUtil.md5(userAccount + userPassword + LocalDateTime.now());
                final String cacheKey = USER_LOGIN_KEY + token;

                final VersionUser versionUser = BeanUtil.copyProperties(user, VersionUser.class);
                versionUser.setVersion(String.valueOf(version));

                final String versionUserJson = JSONUtil.toJsonStr(versionUser);

                redisTemplate.opsForValue().set(cacheKey, versionUserJson, TOKEN_TTL, TimeUnit.MINUTES);
                redisTemplate.expire(versionKey, VERSION_TTL, TimeUnit.HOURS);

                // return token and user
                final TokenUser tokenUser = BeanUtil.copyProperties(user, TokenUser.class);
                tokenUser.setToken(token);
                return tokenUser;
            }


        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "请重新登录");
        } finally {
            // free lock
            lock.unlock();
        }

        return null;
    }

    @Override
    public Boolean logout(User user) {
        return null;
    }

    @Override
    public Long register(UserRegisterRequest userRegisterRequest) {
        return 0L;
    }

    @Override
    public String getByAccessKey(String accessKey) {
        return this.baseMapper.getSecretKey(accessKey);
    }
}




