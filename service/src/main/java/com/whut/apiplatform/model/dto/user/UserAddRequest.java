package com.whut.apiplatform.model.dto.user;

import lombok.Data;

/**
 * @author whut2024
 * @since 2024-08-24
 */

@Data
public class UserAddRequest {


    private String userName;


    private String userAvatar;


    private String userAccount;


    private String userPassword;


    private String userRole;
}
