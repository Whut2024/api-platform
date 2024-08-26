package com.whut.apiplatform.model.dto.interfaceinfo;

import lombok.Data;

/**
 * @author whut2024
 * @since 2024-08-25
 */
@Data
public class InterfaceInfoAddRequest {


    private String url;


    private String name;


    private String description;


    private String method;


    private String requestParam;


    private String responseHeader;


    private String requestHeader;
}
