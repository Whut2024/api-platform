package com.whut.apiplatform.starter;


import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author whut2024
 * @since 2024-08-27
 */
@Configuration
@EnableConfigurationProperties(value = {RequestClientProperties.class})
@AllArgsConstructor
public class RequestClientAutoConfiguration {


    private RequestClientProperties properties;


    @Bean
    public RequestClient requestClient() {
        return new RequestClient(
                properties.getAccessKey(),
                properties.getSecretKey(),
                properties.getServerPort(),
                properties.getServerHost());
    }

}
