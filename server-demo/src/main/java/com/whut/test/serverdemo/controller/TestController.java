package com.whut.test.serverdemo.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author whut2024
 * @since 2024-08-27
 */
@RestController
public class TestController {


    @RequestMapping("/{id}")
    public Object test(@PathVariable("id") Long id) {
        System.out.println(id);
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("aaa", "bbb");

        return hashMap;
    }
}
