package com.whut.apiplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whut.apiplatform.model.entity.InterfaceInfo;
import com.whut.apiplatform.model.entity.User;
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
}
