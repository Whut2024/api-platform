package com.whut.apiplatform.model.dto.interfaceinfo;

import lombok.Data;


/**
 * @author whut2024
 * @since 2024-08-24
 */

@Data
public class InterfaceInfoUpdateRequest {



    private Long id;


    private String name;


    private String url;


    private String description;


    private String method;


    private String requestParam;


    private String responseHeader;


    private String requestHeader;


    private String status;
}
