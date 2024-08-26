package com.whut.apiplatform.core.config;

import lombok.Setter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Setter
public class RedissonConfig {
    private String host;

    private String port;

    private String password;

    @Bean
    public RedissonClient redissonClient() {
        //配置类
        Config config = new Config();
        //添加Redis地址
        String redisAddress = String.format("redis://%s:%s", host, port);
        config.useSingleServer()
                .setAddress(redisAddress)
                .setPassword(password)
                .setDatabase(0);
        //创建Redisson客户端
        return Redisson.create(config);
    }
}