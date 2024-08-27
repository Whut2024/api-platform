package com.whut.apiplatform.door.route;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whut.apiplatform.model.entity.InterfaceInfo;
import com.whut.apiplatform.service.InterfaceInfoService;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author whut2024
 * @since 2024-08-27
 */
@Configuration
@AllArgsConstructor
public class CustomRoute {


    private final RouteLocatorBuilder builder;


    @DubboReference
    private final InterfaceInfoService interfaceInfoService;


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        Long latestId = 0L;
        LambdaQueryWrapper<InterfaceInfo> wrapper;
        while (true) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.gt(InterfaceInfo::getId, latestId);

            List<InterfaceInfo> interfaceInfoList = interfaceInfoService.page(new Page<>(0, 1024), wrapper).getRecords();
            latestId = interfaceInfoList.get(interfaceInfoList.size() - 1).getId();

            RouteLocatorBuilder.Builder routes = builder.routes();

            for (InterfaceInfo interfaceInfo : interfaceInfoList) {
                        routes
                        // 可以继续添加其他路由规则
                        .route(interfaceInfo.getName(), r -> r
                                .path("/**")
                                .filters(f -> f.stripPrefix(0))
                                .uri(interfaceInfo.getUrl()));

            }

            return routes.build();

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
