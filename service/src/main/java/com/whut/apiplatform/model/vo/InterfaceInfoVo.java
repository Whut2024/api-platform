package com.whut.apiplatform.model.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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


    private String url;


    private String method;


    private String description;


    private String requestParam;


    private String requestBody;


    private String requestHeader;


    private String status;


    private Date createTime;


    private Date updateTime;
}
