package com.whut.apiplatform.starter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author whut2024
 * @since 2024-08-27
 */
public class RequestClient {

    private final String accessKey;


    private final String secretKey;


    private final String serverPort;


    private final String serverHost;


    protected RequestClient(String accessKey, String secretKey, String serverPort, String serverHost) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.serverPort = serverPort;
        this.serverHost = serverHost;
    }


    public InvokedResponse invoke(Long interfaceInfoId, String method, Map<String, String> paramMap, Map<String, List<String>> headerMap, String body) {
        return invoke(interfaceInfoId, serverHost, serverPort, method, paramMap, headerMap, body);

    }


    public InvokedResponse invoke(Long interfaceInfoId, String host, String port, String method, Map<String, String> paramMap, Map<String, List<String>> headerMap, String body) {
        final StringBuilder stringBuilder = new StringBuilder(String.format("http://%s:%s/%s", host, port, interfaceInfoId));

        // prepare for params
        if (CollectionUtil.isNotEmpty(paramMap)) {
            stringBuilder.append('?');
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                stringBuilder.append(entry.getKey());
                stringBuilder.append('=');
                stringBuilder.append(entry.getValue());
            }
        }

        // generate url
        final UrlBuilder urlBuilder = UrlBuilder.ofHttp(stringBuilder.toString(), StandardCharsets.UTF_8);

        // set encrypted k-v
        if (headerMap == null)
            headerMap = new HashMap<>();
        headerMap.put("accessKey", CollectionUtil.newArrayList(accessKey));
        headerMap.put("digest", CollectionUtil.newArrayList(SecureUtil.md5(secretKey + accessKey)));

        // prepare for request
        final HttpRequest request = HttpRequest
                .of(urlBuilder)
                .method(Method.valueOf(method.toUpperCase()))
                .header(headerMap)
                .body(body);

        HttpResponse response = null;
        try {
            response = request.execute();

            return new InvokedResponse(response.getStatus(), response.headers(), response.body());
        } catch (HttpException e) {
            throw new RuntimeException(e);
        } finally {
            if (response != null)
                response.close();
        }
    }
}
