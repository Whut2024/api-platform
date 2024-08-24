package com.whut.apiplatform.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author whut2024
 * @since 2024-08-24
 */
@Data
@TableName("user")
public class UserInterfaceInfo {


    @TableId
    private Long id;


    private Long userId;


    private Integer totalNum;


    private Integer leftNum;


    private Date createTime;


    private Date updateTime;


    @TableLogic
    private String isDelete;
}
