package com.example.user.service;

import com.example.user.model.OAuthTokenDTO;

/**
 * 用户相关
 *
 * @author minus
 * @since 2023/9/12 16:20
 */
public interface UserService {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return OAuthTokenDTO
     */
    OAuthTokenDTO login(String username, String password);

}
