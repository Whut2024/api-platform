package com.whut.apipaltform.core.controller;

import com.whut.apiplatform.model.dto.user.UserLoginRequest;
import com.whut.apiplatform.model.dto.user.UserRegisterRequest;
import com.whut.apiplatform.model.entity.User;
import com.whut.apiplatform.model.vo.UserVo;
import com.whut.webs.response.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author whut2024
 * @since 2024-08-25
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @GetMapping("/get/login")
    public BaseResponse<UserVo> getLoginUser() {


        return null;
    }


    @PostMapping("/login")
    public BaseResponse<User> userLogin(UserLoginRequest userLoginRequest) {

        return null;
    }


    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout() {


        return null;
    }


    @PostMapping("/register")
    public BaseResponse<Long> userRegister(UserRegisterRequest userRegisterRequest) {

        return null;
    }

}
