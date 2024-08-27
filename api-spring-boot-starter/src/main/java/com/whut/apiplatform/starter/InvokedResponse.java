package com.whut.apiplatform.starter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * @author whut2024
 * @since 2024-08-27
 */
@Getter
@AllArgsConstructor
public class InvokedResponse {


    private final Integer statusCode;


    private final Map<String, List<String>> headerMap;


    private final String body;
}
