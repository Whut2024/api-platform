package com.whut.apiplatform.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author whut2024
 * @since 2024-08-24
 */
@Data
@TableName("user_interface_info")
public class UserInterfaceInfo {


    @TableId
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;


    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long interfaceInfoId;


    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;


    private Integer totalNum;


    private Integer leftNum;


    private Date createTime;


    private Date updateTime;


    @TableLogic
    private String isDelete;
}
