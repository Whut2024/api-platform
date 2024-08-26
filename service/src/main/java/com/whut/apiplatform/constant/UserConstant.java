package com.whut.apiplatform.constant;

/**
 * @author whut2024
 * @since 2024-08-25
 */
public interface UserConstant {


    String USER_LOGIN_KEY = "user:login:";


    String USER_LOGIN_LOCK_KEY = "user:login:lock:";


    Long USER_LOGIN_LOCK_TTL = 5L;


    String USER_LOGIN_VERSION_KEY = "user:login:version:";


    Long TOKEN_TTL = 30L;
}
