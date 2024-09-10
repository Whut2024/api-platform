package com.whut.apiplatform.core.interceptor;

import com.whut.apiplatform.core.utils.UserHolder;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author whut2024
 * @since 2024-09-10
 */
public class UserInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (request.getMethod().equals(HttpMethod.OPTIONS.name()))
            return true;

        UserHolder.get();

        return true;
    }
}
