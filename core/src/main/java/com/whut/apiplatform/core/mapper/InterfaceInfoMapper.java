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


    /**
     * 根据用户ID获取用户接口信息列表
     *
     * @param userId 用户ID
     * @return 用户接口信息列表
     */
    List<InterfaceInfoVo> getInterfaceInfoVoForUser(Long userId);

    /**
     * only select status by id
     */
    String selectStatus(Long id);


    /**
     * 获取指定ID后的接口信息列表
     *
     * @param latestId 最新接口ID，返回结果中的ID应大于该值
     * @return 接口信息列表，每个接口信息包括接口ID和接口URL
     */
    List<InterfaceInfo> getIdAndUrlList(Long latestId);



    InterfaceInfo getMethodAndUrl(Long id);

}




