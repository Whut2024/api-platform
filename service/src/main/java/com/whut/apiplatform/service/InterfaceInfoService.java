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
}
