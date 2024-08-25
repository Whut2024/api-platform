package com.whut.apiplatform.model.dto.userinterfaceinfo;

import lombok.Data;


/**
 * @author whut2024
 * @since 2024-08-24
 */
@Data
public class UserInterfaceInfoAddRequest {


    private Long interfaceInfoId;


    private Long userId;


    private Integer totalNum;


    private Integer leftNum;

}
