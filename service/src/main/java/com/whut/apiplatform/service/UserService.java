package com.whut.apiplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whut.apiplatform.model.dto.user.UserLoginRequest;
import com.whut.apiplatform.model.dto.user.UserRegisterRequest;
import com.whut.apiplatform.model.entity.User;

/**
* @author laowang
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-08-24 20:59:37
*/
public interface UserService extends IService<User> {

    /**
     * login and generate a token
     */
    User login(UserLoginRequest userLoginRequest);

    /**
     * logout and delete a token
     */
    Boolean logout(User user);

    /**
     * register a user and generate an id
     */
    Long register(UserRegisterRequest userRegisterRequest);

    /**
     * get user information by access key
     */
    String getByAccessKey(String accessKey);
}
