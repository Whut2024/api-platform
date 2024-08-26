package com.whut.apiplatform.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.apiplatform.core.mapper.InterfaceInfoMapper;
import com.whut.apiplatform.core.utils.SqlUtils;
import com.whut.apiplatform.core.utils.UserHolder;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoPageRequest;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.whut.apiplatform.model.entity.InterfaceInfo;
import com.whut.apiplatform.model.entity.User;
import com.whut.apiplatform.model.entity.UserInterfaceInfo;
import com.whut.apiplatform.model.enums.UserRoleEnum;
import com.whut.apiplatform.service.InterfaceInfoService;
import com.whut.apiplatform.service.UserInterfaceInfoService;
import com.whut.common.DeleteRequest;
import com.whut.common.constant.CommonConstant;
import com.whut.webs.exception.ErrorCode;
import com.whut.webs.exception.ThrowUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author laowang
* @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
* @createDate 2024-08-24 20:59:24
*/
@Service
@AllArgsConstructor
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{


    private final UserInterfaceInfoService userInterfaceInfoService;


    @Override
    public void saveInterfaceInfo(InterfaceInfo interfaceInfo, User user) {
        boolean saved = this.save(interfaceInfo);
        ThrowUtils.throwIf(!saved, ErrorCode.OPERATION_ERROR, "请重试");

        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        userInterfaceInfo.setInterfaceInfoId(interfaceInfo.getId());
        userInterfaceInfo.setUserId(user.getId());
        userInterfaceInfo.setLeftNum(0);
        userInterfaceInfo.setTotalNum(0);

        saved = userInterfaceInfoService.save(userInterfaceInfo);
        ThrowUtils.throwIf(!saved, ErrorCode.OPERATION_ERROR, "请重试");
    }

    @Override
    public QueryWrapper<InterfaceInfo> getPageWrapper(InterfaceInfoPageRequest interfaceInfoPageRequest) {
        final QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();

        final Long id = interfaceInfoPageRequest.getId();
        final String url = interfaceInfoPageRequest.getUrl();
        final String description = interfaceInfoPageRequest.getDescription();
        final String method = interfaceInfoPageRequest.getMethod();
        final String requestParam = interfaceInfoPageRequest.getRequestParam();
        final String responseHeader = interfaceInfoPageRequest.getResponseHeader();
        final String requestHeader = interfaceInfoPageRequest.getRequestHeader();
        final String status = interfaceInfoPageRequest.getStatus();
        final String sortField = interfaceInfoPageRequest.getSortField();
        final String sortOrder = interfaceInfoPageRequest.getSortOrder();
        final Long latestId = interfaceInfoPageRequest.getLatestId();

        if (id != null) {
            wrapper.eq("id", id);
            return wrapper;
        } else if (latestId != null)
            wrapper.gt("id", latestId);

        wrapper.eq(url != null, "url", url);
        wrapper.eq(description != null, "description", description);
        wrapper.eq(method != null, "method", method);
        wrapper.eq(requestHeader != null, "request_header", requestHeader);
        wrapper.eq(requestParam != null, "request_param", requestParam);
        wrapper.eq(responseHeader != null, "response_header", responseHeader);
        wrapper.eq(status != null, "status", status);



        wrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);

        return wrapper;
    }

    @Override
    public Boolean updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        final Long id = interfaceInfoUpdateRequest.getId();

        // is admin ?
        User user = UserHolder.get();

        checkRole(user, id);

        // update
        InterfaceInfo interfaceInfo = BeanUtil.copyProperties(interfaceInfoUpdateRequest, InterfaceInfo.class);
        return this.updateById(interfaceInfo);
    }

    @Override
    public Boolean deleteInterfaceInfo(DeleteRequest deleteRequest) {
        final Long id = deleteRequest.getId();

        User user = UserHolder.get();
        checkRole(user, id);

        return this.removeById(id);
    }


    public void checkRole(User user, Long interfaceInfoId) {
        if (!UserRoleEnum.isAdmin(user)) {
            // whether it contains this
            LambdaQueryWrapper<UserInterfaceInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId);

            UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getOne(wrapper);

            ThrowUtils.throwIf(userInterfaceInfo == null, ErrorCode.PARAMS_ERROR, "接口信息不存在");
            ThrowUtils.throwIf(user.getId().equals(userInterfaceInfo.getUserId()), ErrorCode.NO_AUTH_ERROR);
        }
    }
}




