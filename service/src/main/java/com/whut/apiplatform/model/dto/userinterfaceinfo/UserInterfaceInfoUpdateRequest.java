package com.whut.apiplatform.model.dto.userinterfaceinfo;

import lombok.Data;


/**
 * @author whut2024
 * @since 2024-08-24
 */
@Data
public class UserInterfaceInfoUpdateRequest {


    private Long id;


    private Integer totalNum;


    private Integer leftNum;


    private String status;
}
