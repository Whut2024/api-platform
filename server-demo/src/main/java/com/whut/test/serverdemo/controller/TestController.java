package com.whut.test.serverdemo.controller;

import cn.hutool.json.JSONUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author whut2024
 * @since 2024-08-27
 */
@RestController
public class TestController {


    @RequestMapping("/{id}")
    public Object test(@PathVariable("id") Long id, HttpServletRequest request) throws Exception {


        int len = request.getIntHeader("content-length");
        final ServletInputStream inputStream = request.getInputStream();
        if (len < 0) len = 0;
        final byte[] byteArray = new byte[len];

        inputStream.read(byteArray);

        final Enumeration<String> enumeration = request.getHeaderNames();
        final Map<String, List<String>> headerMap = new HashMap<>();
        while (enumeration.hasMoreElements()) {
            final String key = enumeration.nextElement();

            final List<String> headerList = headerMap.getOrDefault(key, new ArrayList<>());
            headerList.add(request.getHeader(key));
            headerMap.put(key, headerList);
        }


        final Map<String, String[]> parameterMap = request.getParameterMap();
        final String parameterMapJsonStr = JSONUtil.toJsonStr(parameterMap);


        return id + "\n" +
                parameterMapJsonStr + "\n" +
                headerMap + "\n" +
                new String(byteArray, StandardCharsets.UTF_8);
    }
}
