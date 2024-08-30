package com.whut.apiplatform.model.dto.interfaceinfo;

import lombok.Data;

/**
 * @author whut2024
 * @since 2024-08-25
 */

@Data
public class InterfaceInfoInvokeRequest {


    private Long id;


    private String requestParamJson;


    private String requestBodyStr;


    private String requestHeaderJson;
}
