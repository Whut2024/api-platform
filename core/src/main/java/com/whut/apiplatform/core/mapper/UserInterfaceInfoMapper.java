package com.whut.apiplatform.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whut.apiplatform.model.entity.UserInterfaceInfo;

/**
* @author laowang
* @description 针对表【user_interface_info(用户接口表)】的数据库操作Mapper
* @createDate 2024-08-24 20:59:41
* @Entity xx.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    Integer getLeftNum(Long interfaceInfoId);
}




