package com.whut.apiplatform.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoPageRequest;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.whut.apiplatform.model.entity.InterfaceInfo;
import com.whut.apiplatform.model.entity.User;
import com.whut.common.DeleteRequest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author laowang
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service
 * @createDate 2024-08-24 20:59:24
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * insert interface info and user interface info to database
     */
    @Transactional
    void saveInterfaceInfo(InterfaceInfo interfaceInfo, User user);

    /**
     * get a wrapper form variables
     */
    QueryWrapper<InterfaceInfo> getPageWrapper(InterfaceInfoPageRequest interfaceInfoPageRequest);


    /**
     * update an interface info
     */
    Boolean updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest);

    /**
     * delete an interface info
     */
    Boolean deleteInterfaceInfo(DeleteRequest deleteRequest);

    /**
     * just check whether the target interface info exists in the table
     */
    Boolean checkExistenceById(String id);

    /**
     * get interface info's id and url arrayStr
     */
    String getIdAndUrlStr(Long latestId);

    /**
     * offline an interface info
     */
    Boolean offlineInterfaceInfo(Long id);

    /**
     * online an interface info
     */
    Boolean onlineInterfaceInfo(Long id);


    /**
     * 调用指定ID的接口
     *
     * @param id 接口ID
     * @param requestParamJson 请求参数JSON字符串
     * @param requestHeaderJson 请求头JSON字符串
     * @param requestBodyStr 请求体JSON字符串
     * @return 接口调用结果对象
     */
    Object invoke(Long id, String requestParamJson, String requestHeaderJson, String requestBodyStr);
}
