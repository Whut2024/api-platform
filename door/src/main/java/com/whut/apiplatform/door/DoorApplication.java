package com.whut.apiplatform.door;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class DoorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoorApplication.class, args);
    }

}
