package com.whut.apiplatform.core.controller;

import cn.hutool.core.util.StrUtil;
import com.whut.apiplatform.core.utils.UserHolder;
import com.whut.apiplatform.model.dto.user.UserLoginRequest;
import com.whut.apiplatform.model.dto.user.UserRegisterRequest;
import com.whut.apiplatform.model.entity.User;
import com.whut.apiplatform.model.vo.UserVo;
import com.whut.apiplatform.service.UserService;
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
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {


    private final UserService userService;


    @GetMapping("/get/login")
    public BaseResponse<UserVo> getLoginUser() {
        User user = UserHolder.get();

        return ResultUtils.success(UserVo.getInstance(user));
    }


    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody  UserLoginRequest userLoginRequest) {
        // todo check params

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();


        ThrowUtils.throwIf(!StrUtil.isAllNotBlank(userAccount, userPassword), ErrorCode.PARAMS_ERROR);

        User user = userService.login(userLoginRequest);
        return ResultUtils.success(user);
    }


    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout() {

        User user = UserHolder.get();

        Boolean success = userService.logout(user);

        return ResultUtils.success(success);
    }


    @PostMapping("/register")
    public BaseResponse<Long> userRegister(UserRegisterRequest userRegisterRequest) {
        String checkPassword = userRegisterRequest.getCheckPassword();
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();

        ThrowUtils.throwIf(
                !StrUtil.isAllNotBlank(checkPassword, userAccount, userPassword) ||
                        !userPassword.equals(checkPassword),
                ErrorCode.PARAMS_ERROR);

        Long id = userService.register(userRegisterRequest);

        return ResultUtils.success(id);
    }

}
