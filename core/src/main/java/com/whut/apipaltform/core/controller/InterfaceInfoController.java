package com.whut.apipaltform.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoPageRequest;
import com.whut.apiplatform.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import com.whut.common.DeleteRequest;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.whut.apiplatform.model.entity.InterfaceInfo;
import com.whut.common.IdRequest;
import com.whut.webs.response.BaseResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @author whut2024
 * @since 2024-08-25
 */
@RestController
@RequestMapping("/interfaceInfo")
public class InterfaceInfoController {


    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(InterfaceInfoAddRequest interfaceInfoAddRequest) {


        return null;
    }


    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(DeleteRequest deleteRequest) {


        return null;
    }


    @GetMapping("/get/{id}")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(@PathVariable("id") Long id) {


        return null;
    }


    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(InterfaceInfoInvokeRequest interfaceInfoInvokeRequest) {

        return null;
    }


    @GetMapping("/list")
    public Page<InterfaceInfo> listInterfaceInfoByPage(@RequestParam InterfaceInfoPageRequest interfaceInfoPageRequest) {
        return null;
    }


    @PostMapping("/offline")
    public BaseResponse<Boolean> offlineInterfaceInfo(IdRequest idRequest) {


        return null;
    }


    @PostMapping("/online")
    public BaseResponse<Boolean> onlineInterfaceInfo(IdRequest idRequest) {


        return null;
    }


    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(UserInterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        return null;
    }


}
