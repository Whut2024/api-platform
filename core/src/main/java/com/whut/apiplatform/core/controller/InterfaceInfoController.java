package com.whut.apiplatform.core.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whut.apiplatform.core.utils.UserHolder;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoPageRequest;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.whut.apiplatform.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import com.whut.apiplatform.model.entity.User;
import com.whut.apiplatform.model.entity.UserInterfaceInfo;
import com.whut.apiplatform.model.enums.UserRoleEnum;
import com.whut.apiplatform.service.InterfaceInfoService;
import com.whut.apiplatform.service.UserInterfaceInfoService;
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
        final String url = interfaceInfoAddRequest.getUrl();
        final String description = interfaceInfoAddRequest.getDescription();
        final String method = interfaceInfoAddRequest.getMethod();
        final String requestParam = interfaceInfoAddRequest.getRequestParam();
        final String responseHeader = interfaceInfoAddRequest.getResponseHeader();
        final String requestHeader = interfaceInfoAddRequest.getRequestHeader();

        ThrowUtils.throwIf(!StrUtil.isAllNotBlank(url, description, method, responseHeader, requestParam, requestHeader), ErrorCode.PARAMS_ERROR);

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
    public BaseResponse<Object> invokeInterfaceInfo(InterfaceInfoInvokeRequest interfaceInfoInvokeRequest) {

        return null;
    }


    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(@ModelAttribute InterfaceInfoPageRequest interfaceInfoPageRequest) {
        final int pageSize = interfaceInfoPageRequest.getPageSize();

        final QueryWrapper<InterfaceInfo> wrapper = interfaceInfoService.getPageWrapper(interfaceInfoPageRequest);
        Page<InterfaceInfo> page = interfaceInfoService.page(new Page<>(0, pageSize), wrapper);

        return ResultUtils.success(page);
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
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        final Long id = interfaceInfoUpdateRequest.getId();

        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);

        final Boolean updated = interfaceInfoService.updateInterfaceInfo(interfaceInfoUpdateRequest);
        return ResultUtils.success(updated);
    }


}
