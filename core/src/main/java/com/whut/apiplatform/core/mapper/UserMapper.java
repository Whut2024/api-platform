package com.whut.apiplatform.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whut.apiplatform.model.entity.User;

/**
* @author laowang
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2024-08-25 20:56:43
* @Entity xx.domain.User
*/
public interface UserMapper extends BaseMapper<User> {

    String getSecretKey(String accessKey);
}




