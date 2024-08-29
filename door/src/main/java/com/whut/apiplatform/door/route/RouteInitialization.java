package com.whut.apiplatform.door.route;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whut.apiplatform.model.entity.InterfaceInfo;
import com.whut.apiplatform.service.InterfaceInfoService;
import com.whut.apiplatform.service.RouteService;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author whut2024
 * @since 2024-08-27
 */
@Component
@AllArgsConstructor
public class RouteInitialization {


    @DubboReference
    private final InterfaceInfoService interfaceInfoService;


    private final RouteService routeService;


    @PostConstruct
    void init() {
        long latestId = 0L;


        while (true) {
            final String idAndUrlStr = interfaceInfoService.getIdAndUrlStr(latestId);
            if (StrUtil.isBlank(idAndUrlStr)) break;

            String[] idUrlArray = idAndUrlStr.split(" ");

            latestId = Long.parseLong(idUrlArray[idUrlArray.length - 2]);

            long id = 0L;
            for (int i = 0; i < idUrlArray.length; i++) {

                if (i % 2 == 0)
                    id = Long.parseLong(idUrlArray[i]);
                else
                    routeService.add(String.valueOf(id), idUrlArray[i]);
            }
        }

        /*return builder.routes()
                // 定义一个路由：id为my_route，当请求路径为/api/service/**时，转发到http://localhost:8080
                .route("my_route", r -> r
                        .path("/**")
                        .filters(f -> f.stripPrefix(0)) // 移除路径的前两个前缀段，即/api/service
                        .uri("http://localhost:7531"))
                // 可以继续添加其他路由规则
                .build();*/

    }
}
