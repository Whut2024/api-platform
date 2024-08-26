package com.whut.apiplatform.core.config;

import com.whut.apiplatform.core.interceptor.LoginInterceptor;
import lombok.AllArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author whut2024
 * @since 2024-08-25
 */
@Configuration
@AllArgsConstructor
public class CustomMvcConfig implements WebMvcConfigurer {


    private final StringRedisTemplate redisTemplate;


    private final RedissonClient redissonClient;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(redisTemplate, redissonClient)).excludePathPatterns("/user/login");
    }
}
