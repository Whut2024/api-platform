package com.whut.apiplatform.door.service.impl;

import com.whut.apiplatform.service.RouteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author whut2024
 * @since 2024-08-29
 */
@DubboService
@AllArgsConstructor
@Slf4j
public class RouteServiceImpl implements RouteService {


    private RouteDefinitionWriter routeDefinitionWriter;


    @Override
    public void update(String id, String newUrl) {
        final RouteDefinition routeDefinition = getRouteDefinition(id, newUrl);
        log.info("更新路由配置项：{}", routeDefinition);
        this.delete(id);
        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
    }

    @Override
    public void add(String id, String newUrl) {
        final RouteDefinition routeDefinition = getRouteDefinition(id, newUrl);
        log.info("新增路由配置项：{}", routeDefinition);
        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
    }

    @Override
    public void delete(String id) {
        log.info("删除ID={}的路由", id);
        routeDefinitionWriter.delete(Mono.just(id)).subscribe();
    }


    private RouteDefinition getRouteDefinition(String id, String newUrl) {
        // 路由
        final RouteDefinition routeDefinition = new RouteDefinition();

        /*
         断言
        final PredicateDefinition predicateDefinition = new PredicateDefinition();
        predicateDefinition.addArg(RouteConstant.GEN_KEY + 0, "id");
        predicateDefinition.addArg(RouteConstant.GEN_KEY + 1, id);
        predicateDefinition.setName(RouteConstant.HEADER);
        routeDefinition.setPredicates(Collections.singletonList(predicateDefinition));*/



        // 路由配置
        routeDefinition.setId(id);
        try {
            routeDefinition.setUri(new URI(newUrl));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return routeDefinition;
    }

}
