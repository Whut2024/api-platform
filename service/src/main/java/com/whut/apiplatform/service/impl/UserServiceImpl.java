package com.whut.apiplatform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.apiplatform.mapper.UserMapper;
import com.whut.apiplatform.model.entity.User;
import com.whut.apiplatform.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author laowang
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-08-24 20:59:37
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




