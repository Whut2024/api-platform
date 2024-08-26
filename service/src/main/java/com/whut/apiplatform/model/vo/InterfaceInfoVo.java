package com.whut.apiplatform.model.vo;


import lombok.Data;

import java.util.Date;

/**
 * @author whut2024
 * @since 2024-08-25
 */
@Data
public class InterfaceInfoVo {


    private Long id;


    private Long userId;


    private String name;


    private String url;


    private String method;


    private String description;


    private String requestParam;


    private String responseHeader;


    private String requestHeader;


    private String status;


    private Integer totalNum;


    private Date createTime;


    private Date updateTime;


    private String isDelete;
}
