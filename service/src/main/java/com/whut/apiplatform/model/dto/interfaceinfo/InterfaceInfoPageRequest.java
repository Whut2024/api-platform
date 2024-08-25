package com.whut.apiplatform.model.dto.interfaceinfo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.whut.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author whut2024
 * @since 2024-08-25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoPageRequest extends PageRequest {


    private Long id;


    private String url;


    private String description;


    private String method;


    private String requestParam;


    private String requestBody;


    private String requestHeader;


    private String status;
}
