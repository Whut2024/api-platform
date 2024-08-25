package com.whut.apiplatform.model.dto.user;

import lombok.Data;


/**
 * @author whut2024
 * @since 2024-08-24
 */

@Data
public class UserRegisterRequest {


    private String checkPassword;


    private String userAccount;


    private String userPassword;

}
