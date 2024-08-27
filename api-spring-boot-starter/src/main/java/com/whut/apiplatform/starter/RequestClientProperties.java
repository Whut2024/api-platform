package com.whut.apiplatform.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author whut2024
 * @since 2024-08-27
 */
@ConfigurationProperties(prefix = "api-platform")
@Data
public class RequestClientProperties {


    private String accessKey;


    private String secretKey;


    private String serverPort;


    private String serverHost;
}
