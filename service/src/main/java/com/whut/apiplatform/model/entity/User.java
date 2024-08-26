package com.whut.apiplatform.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author whut2024
 * @since 2024-08-24
 */

@Data
@TableName("user")
public class User implements Serializable {

    @TableId
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;


    private String userName;


    private String userAvatar;


    private String userAccount;


    private String userPassword;


    private String userRole;


    private String accessKey;


    private String secretKey;


    private Date createTime;


    private Date updateTime;


    @TableLogic
    private String isDelete;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
