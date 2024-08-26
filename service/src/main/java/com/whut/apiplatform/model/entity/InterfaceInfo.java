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
@TableName("interface_info")
public class InterfaceInfo {


    @TableId
    private Long id;


    private String url;


    private String description;


    private String method;


    private String requestParam;


    private String responseHeader;


    private String requestHeader;


    private String status;


    private Date createTime;


    private Date updateTime;


    @TableLogic
    private String isDelete;
}
