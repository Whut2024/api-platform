package com.whut.apiplatform.model.dto.user;

import com.whut.apiplatform.model.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author whut2024
 * @since 2024-08-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VersionUser extends User {

    private String version;
}
