package com.whut.apiplatform.core.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whut.apiplatform.core.utils.UserHolder;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoPageRequest;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.whut.apiplatform.model.entity.User;
import com.whut.apiplatform.service.InterfaceInfoService;
import com.whut.common.DeleteRequest;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.whut.apiplatform.model.entity.InterfaceInfo;
import com.whut.common.IdRequest;
import com.whut.starter.exception.ErrorCode;
import com.whut.starter.exception.ThrowUtils;
import com.whut.starter.response.BaseResponse;
import com.whut.starter.response.ResultUtils;
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
        final String url = interfaceInfoAddRequest.getUrl();
        final String name = interfaceInfoAddRequest.getName();
        final String description = interfaceInfoAddRequest.getDescription();
        final String method = interfaceInfoAddRequest.getMethod();
        final String requestParam = interfaceInfoAddRequest.getRequestParam();
        final String requestHeader = interfaceInfoAddRequest.getRequestHeader();
        final String requestBody = interfaceInfoAddRequest.getRequestBody();
        final String responseHeader = interfaceInfoAddRequest.getResponseHeader();
        final String responseBody = interfaceInfoAddRequest.getResponseBody();


        ThrowUtils.throwIf(!StrUtil.isAllNotBlank(name, requestBody, responseBody, url, description, method, responseHeader, requestParam, requestHeader), ErrorCode.PARAMS_ERROR);

        final User user = UserHolder.get();

        final InterfaceInfo interfaceInfo = BeanUtil.copyProperties(interfaceInfoAddRequest, InterfaceInfo.class);

        interfaceInfoService.saveInterfaceInfo(interfaceInfo, user);

        return ResultUtils.success(interfaceInfo.getId());
    }


    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest) {
        final Long id = deleteRequest.getId();

        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);

        Boolean deleted = interfaceInfoService.deleteInterfaceInfo(deleteRequest);
        return ResultUtils.success(deleted);

    }


    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(@RequestParam("id") Long id) {

        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);

        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);

        return ResultUtils.success(interfaceInfo);
    }


    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest) {
        final Long id = interfaceInfoInvokeRequest.getId();
        final String requestParamJson = interfaceInfoInvokeRequest.getRequestParamJson();
        final String requestBodyStr = interfaceInfoInvokeRequest.getRequestBodyStr();
        final String requestHeaderJson = interfaceInfoInvokeRequest.getRequestHeaderJson();

        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);

        final Object invokedResponse = this.interfaceInfoService.invoke(id, requestParamJson, requestHeaderJson, requestBodyStr);

        final String jsonStr = JSONUtil.toJsonStr(invokedResponse);

        return ResultUtils.success(jsonStr);
    }


    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(@ModelAttribute InterfaceInfoPageRequest interfaceInfoPageRequest) {
        final int pageSize = interfaceInfoPageRequest.getPageSize();

        final QueryWrapper<InterfaceInfo> wrapper = interfaceInfoService.getPageWrapper(interfaceInfoPageRequest);
        Page<InterfaceInfo> page = interfaceInfoService.page(new Page<>(0, pageSize), wrapper);

        return ResultUtils.success(page);
    }


    @PostMapping("/offline")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        final Long id = idRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);

        final Boolean result = interfaceInfoService.offlineInterfaceInfo(idRequest.getId());
        return ResultUtils.success(result);
    }


    @PostMapping("/online")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        final Long id = idRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);

        final Boolean result = interfaceInfoService.onlineInterfaceInfo(idRequest.getId());
        return ResultUtils.success(result);
    }


    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        final Long id = interfaceInfoUpdateRequest.getId();

        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);

        final Boolean updated = interfaceInfoService.updateInterfaceInfo(interfaceInfoUpdateRequest);
        return ResultUtils.success(updated);
    }


}
