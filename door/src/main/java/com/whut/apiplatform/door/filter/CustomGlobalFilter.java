package com.whut.apiplatform.door.filter;

import cn.hutool.crypto.SecureUtil;
import com.whut.apiplatform.model.entity.InterfaceInfo;
import com.whut.apiplatform.model.entity.User;
import com.whut.apiplatform.service.InterfaceInfoService;
import com.whut.apiplatform.service.UserInterfaceInfoService;
import com.whut.apiplatform.service.UserService;
import com.whut.webs.exception.BusinessException;
import com.whut.webs.exception.ErrorCode;
import com.whut.webs.exception.ThrowUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * it will check headers: accessKey, secretKey, interfaceInfoId, digest
 *
 * @author whut2024
 * @since 2024-08-27
 */

@Slf4j
@Component
@AllArgsConstructor
public class CustomGlobalFilter implements GlobalFilter {


    @DubboReference
    private final InterfaceInfoService interfaceInfoService;


    @DubboReference
    private final UserInterfaceInfoService userInterfaceInfoService;


    @DubboReference
    private final UserService userService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        final ServerHttpResponse response = exchange.getResponse();

        // 获取请求头信息
        HttpHeaders headers = request.getHeaders();
        final String accessKey;
        final String digest;
        final String id;
        try {
            accessKey = headers.get("accessKey").get(0);
            digest = headers.get("digest").get(0);
            id = request.getURI().toString().split("/")[3];
        } catch (Exception e) {
           throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数缺失");
        }

        // 并发获取对象
        final User user = userService.getByAccessKey(accessKey);
        final Boolean online = interfaceInfoService.checkExistenceById(id);

        // 校验用户信息
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, "用户信息不存在");
        final String realDigest = SecureUtil.md5(user.getSecretKey() + accessKey);
        ThrowUtils.throwIf(!realDigest.equals(digest), ErrorCode.PARAMS_ERROR, "参数错误");

        // 校验接口信息
        ThrowUtils.throwIf(online, ErrorCode.PARAMS_ERROR, "接口信息不存在或者已经下线");

        // 校验接口剩余量
        Boolean hasLeast = userInterfaceInfoService.checkLeast(id);
        ThrowUtils.throwIf(!hasLeast, ErrorCode.OPERATION_ERROR, "接口暂时无请求次数");

        return chain.filter(exchange);
    }


    /*private Mono<Void> getIllegalResponse(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        response.
    }*/
}


