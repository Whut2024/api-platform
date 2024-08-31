package com.whut.apiplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whut.apiplatform.model.entity.UserInterfaceInfo;

/**
* @author laowang
* @description 针对表【user_interface_info(用户接口表)】的数据库操作Service
* @createDate 2024-08-24 20:59:41
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * check whether the target interface info's invokable number is bigger than zero.
     * If it is bigger, it will be subtracted
     */
    Boolean checkLeast(String interfaceInfoId);


    /**
     * 根据接口信息ID获取用户界面信息
     *
     * @param interfaceInfoId 接口信息ID
     * @return 返回对应的用户界面信息
     */
    UserInterfaceInfo getUserIdByInterfaceInfoId(Long interfaceInfoId);

}
