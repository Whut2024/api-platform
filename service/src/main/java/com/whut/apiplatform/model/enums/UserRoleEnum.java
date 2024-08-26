package com.whut.apiplatform.model.enums;

import com.whut.apiplatform.model.entity.User;
import lombok.AllArgsConstructor;

/**
 * @author whut2024
 * @since 2024-08-26
 */
@AllArgsConstructor
public enum UserRoleEnum {

    ADMIN("admin"),
    USER("user");


    private final String ROLE;


    public static boolean isAdmin(User user) {
        return ADMIN.ROLE.equals(user.getUserRole());
    }
}
