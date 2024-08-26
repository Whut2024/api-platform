package com.whut.apiplatform.core.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whut.apiplatform.core.utils.UserHolder;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoPageRequest;
import com.whut.apiplatform.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import com.whut.apiplatform.model.entity.User;
import com.whut.apiplatform.service.InterfaceInfoService;
import com.whut.common.DeleteRequest;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.whut.apiplatform.model.entity.InterfaceInfo;
import com.whut.common.IdRequest;
import com.whut.webs.exception.ErrorCode;
import com.whut.webs.exception.ThrowUtils;
import com.whut.webs.response.BaseResponse;
import com.whut.webs.response.ResultUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author whut2024
 * @since 2024-08-25
 */
@RestController
@RequestMapping("/interfaceInfo")
@AllArgsConstructor
public class InterfaceInfoController {


    private final InterfaceInfoService interfaceInfoService;


    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest) {
        String url = interfaceInfoAddRequest.getUrl();
        String description = interfaceInfoAddRequest.getDescription();
        String method = interfaceInfoAddRequest.getMethod();
        String requestParam = interfaceInfoAddRequest.getRequestParam();
        String responseHeader = interfaceInfoAddRequest.getResponseHeader();
        String requestHeader = interfaceInfoAddRequest.getRequestHeader();

        ThrowUtils.throwIf(!StrUtil.isAllNotBlank(url, description, method, responseHeader, requestParam, requestHeader), ErrorCode.PARAMS_ERROR);

        User user = UserHolder.get();

        InterfaceInfo interfaceInfo = BeanUtil.copyProperties(interfaceInfoAddRequest, InterfaceInfo.class);

        interfaceInfoService.saveInterfaceInfo(interfaceInfo, user);

        return ResultUtils.success(interfaceInfo.getId());
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

        return new Page<>();
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
