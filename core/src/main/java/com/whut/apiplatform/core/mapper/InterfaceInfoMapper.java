package com.whut.apiplatform.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whut.apiplatform.model.entity.InterfaceInfo;
import com.whut.apiplatform.model.vo.InterfaceInfoVo;

import java.util.List;

/**
* @author laowang
* @description 针对表【interface_info(接口信息表)】的数据库操作Mapper
* @createDate 2024-08-24 20:59:24
* @Entity xx.domain.InterfaceInfo
*/
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {

    /**
     * it is designed for selecting the largest top 3 invoked times for interfaces
     */
    List<InterfaceInfoVo> getInterfaceInfoVoForAdmin();


    List<InterfaceInfoVo> getInterfaceInfoVoForUser(Long userId);

}




