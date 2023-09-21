package com.example.user.service.impl;

import com.example.common.domain.AccountUser;
import com.example.user.domain.SysUser;
import com.example.user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文件名：UserDetailServiceImpl.java
 * 描述：获得数据库的用户信息，security 得到用户信息后，用于登陆验证
 * 时间：2021-07-03
 * 作者：TechRice
 *
 * @author minus
 * @since 2023-09-21
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;    // 注入 SysUserService 层，获取用户基本信息


    // 通过用户名获得用户信息并返回，若不存在则抛出异常
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 调用 Service 层，通过用户名查询
        SysUser sysUser = sysUserService.getUserByName(s);

        // 为空，说明用户不存在
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 将查询出的 SysUser 对象转换为 UserDetails 对象，并返回
        return new AccountUser(sysUser.getId(), sysUser.getUsername(), sysUser.getPassword(), this.getUserAuthority(sysUser));
    }

    // 获取用户权限（角色、菜单权限）
    public List<GrantedAuthority> getUserAuthority(SysUser sysUser) {
        // 角色(ROLE_admin)、菜单操作权限 sys:user:list（返回的格式：ROLE_admin,ROLE_normal,sys:user:list,....）
        String authority = sysUserService.getUserAuthorityInfo(sysUser);

        // 转化为 List 并返回
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
