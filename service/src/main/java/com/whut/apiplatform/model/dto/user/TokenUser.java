package com.whut.apiplatform.model.dto.user;

import com.whut.apiplatform.model.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author whut2024
 * @since 2024-08-25
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class TokenUser extends User {


    private String token;
}
