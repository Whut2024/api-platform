package com.whut.apiplatform.service;

/**
 * @author whut2024
 * @since 2024-08-29
 */
public interface RouteService {


    /**
     * 更新路由配置
     */
    void update(String id, String newUrl);

    /**
     * 添加路由配置
     */
    void add(String id, String newUrl);


    /**
     * 删除路由配置
     */
    void delete(String id);


}
