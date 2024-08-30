package com.whut.test.serverdemo.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @author whut2024
 * @since 2024-08-27
 */
@RestController
public class TestController {


    @RequestMapping("/{id}")
    public Object test(@PathVariable("id") Long id, HttpServletRequest request) throws Exception {


        final ServletInputStream inputStream = request.getInputStream();
        final byte[] byteArray = new byte[4096];


        final int len = inputStream.read(byteArray);


        return request.getParameterMap().toString() + "\n" +
                request.getHeaderNames().toString() + "\n" +
                new String(byteArray, 0, len, StandardCharsets.UTF_8);
    }
}
