package com.whut.apiplatform.core.utils;

import com.whut.apiplatform.model.entity.User;
import com.whut.webs.exception.ErrorCode;
import com.whut.webs.exception.ThrowUtils;

/**
 * store use in a thread map
 * @author whut2024
 * @since 2024-08-25
 */
public class UserHolder {


    private final static ThreadLocal<User> holder = new ThreadLocal<>();


    public static void set(User user) {
        holder.set(user);
    }


    public static User get() {
        User user = holder.get();
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_LOGIN_ERROR);

        return user;
    }
}
