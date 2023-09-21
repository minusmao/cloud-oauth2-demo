package com.example.user.service;

import com.example.user.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.user.model.OAuthTokenDTO;

/**
 * 针对表【sys_user(用户表)】的数据库操作Service
 *
 * @author minus
 * @since 2023-09-12 21:02:39
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return OAuthTokenDTO
     */
    OAuthTokenDTO login(String username, String password);

    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return SysUser
     */
    SysUser getUserByName(String username);


    String getUserAuthorityInfo(SysUser sysUser);

}
