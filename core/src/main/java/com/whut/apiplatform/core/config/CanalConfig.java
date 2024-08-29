package com.whut.apiplatform.core.config;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
@ConfigurationProperties(prefix = "canal")
@Data
public class CanalConfig {

    private String host;

    private int port;

    private String destination;

    private String username;

    private String password;

    @Bean
    public CanalConnector createCanalConnector() {
        // create a connection
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(new InetSocketAddress(host, port), destination, username, password);

        // open it
        canalConnector.connect();

        // subscribe target table
        canalConnector.subscribe(".*\\..*");

        //roll back to the beginning of non-acked sites
        canalConnector.rollback();

        return canalConnector;
    }
}