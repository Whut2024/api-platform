package com.whut.apiplatform.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.whut.apiplatform.model.entity.User;
import lombok.Data;

import java.util.Date;

/**
 * @author whut2024
 * @since 2024-08-25
 */
@Data
public class UserVo {


    private Long id;


    private String userName;


    private String userAvatar;


    private String userAccount;


    private String userRole;


    private Date createTime;


    private Date updateTime;


    public static UserVo getInstance(User user) {
        return BeanUtil.copyProperties(user, UserVo.class);
    }
}
