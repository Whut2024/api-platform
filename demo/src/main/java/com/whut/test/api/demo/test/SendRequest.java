package com.whut.test.api.demo.test;

import cn.hutool.core.collection.CollectionUtil;
import com.whut.apiplatform.starter.InvokedResponse;
import com.whut.apiplatform.starter.RequestClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author whut2024
 * @since 2024-08-27
 */
@Component
@AllArgsConstructor
public class SendRequest {


    private final RequestClient requestClient;


    @PostConstruct
    void init() {

        final Map<String, List<String>> headerMap = new HashMap<>();


        InvokedResponse invokedResponse = requestClient.invoke(1828614577263648770L,"get", null, headerMap, "string");

        System.out.println(invokedResponse);


    }
}
