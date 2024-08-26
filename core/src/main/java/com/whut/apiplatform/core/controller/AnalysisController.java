package com.whut.apiplatform.core.controller;

import com.whut.apiplatform.core.mapper.InterfaceInfoMapper;
import com.whut.apiplatform.core.utils.UserHolder;
import com.whut.apiplatform.model.entity.User;
import com.whut.apiplatform.model.enums.UserRoleEnum;
import com.whut.apiplatform.model.vo.InterfaceInfoVo;
import com.whut.webs.exception.ErrorCode;
import com.whut.webs.exception.ThrowUtils;
import com.whut.webs.response.BaseResponse;
import com.whut.webs.response.ResultUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author whut2024
 * @since 2024-08-25
 */
@RestController
@RequestMapping("/analysis")
@AllArgsConstructor
public class AnalysisController {


    private final InterfaceInfoMapper interfaceInfoMapper;


    @GetMapping("/top/interface/invoke")
    public BaseResponse<List<InterfaceInfoVo>> listTopInvokedInterfaceInfo() {
        // check role
        User user = UserHolder.get();

        // get top 3 from user_interface_info

        List<InterfaceInfoVo> interfaceInfoVoList;

        if (UserRoleEnum.isAdmin(user))
            interfaceInfoVoList = interfaceInfoMapper.getInterfaceInfoVoForAdmin();
        else
            interfaceInfoVoList = interfaceInfoMapper.getInterfaceInfoVoForUser(user.getId());


        return ResultUtils.success(interfaceInfoVoList);
    }
}
