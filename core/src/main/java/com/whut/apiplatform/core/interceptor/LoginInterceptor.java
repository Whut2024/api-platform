package com.whut.apiplatform.core.interceptor;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.whut.apiplatform.constant.UserConstant;
import com.whut.apiplatform.core.utils.UserHolder;
import com.whut.apiplatform.model.dto.user.VersionUser;
import com.whut.webs.exception.BusinessException;
import com.whut.webs.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

import static com.whut.apiplatform.constant.UserConstant.*;

/**
 * check whether it logs-in and store user's message
 *
 * @author whut2024
 * @since 2024-08-25
 */

@Slf4j
@AllArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {


    private final StringRedisTemplate redisTemplate;


    private final RedissonClient redissonClient;


    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {

        if (request.getMethod().equals("OPTIONS")) return true;

        // get token form headers
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) return true;

        // get json value from redis
        final String cacheKey = USER_LOGIN_KEY + token;
        String userJson = redisTemplate.opsForValue().get(cacheKey);
        if (StrUtil.isBlank(userJson)) return true;
        VersionUser user = JSONUtil.toBean(userJson, VersionUser.class);

        // todo whether we should lock it

        // get login version and check it
        final String versionKey = USER_LOGIN_VERSION_KEY + user.getId();
        final String versionValue = redisTemplate.opsForValue().get(versionKey);
        if (user.getVersion().equals(versionValue)) {
            // store it
            UserHolder.set(user);

            // refresh expiration
            redisTemplate.expire(cacheKey, TOKEN_TTL, TimeUnit.MINUTES);
        }


        return true;
    }
}
