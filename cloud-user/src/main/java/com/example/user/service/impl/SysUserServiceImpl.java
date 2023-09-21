package com.example.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.client.CloudAuthClient;
import com.example.user.domain.SysMenu;
import com.example.user.domain.SysRole;
import com.example.user.domain.SysUser;
import com.example.user.model.OAuthTokenDTO;
import com.example.user.service.SysMenuService;
import com.example.user.service.SysRoleService;
import com.example.user.service.SysUserService;
import com.example.user.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 针对表【sys_user(用户表)】的数据库操作Service实现
 *
 * @author minus
 * @since 2023-09-12 21:02:39
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private CloudAuthClient cloudAuthClient;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    @Value("${auth.client-id}")
    private String clientId;

    @Value("${auth.client-secret}")
    private String clientSecret;

    @Override
    public OAuthTokenDTO login(String username, String password) {
        String authorization = "Basic " + Base64.getEncoder().encodeToString((clientId + ':' + clientSecret).getBytes());
        return cloudAuthClient.getToken(authorization, "password", username, password);
    }

    @Override
    public SysUser getUserByName(String username) {
        return this.getOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    // 通过用户 id 查找用户角色、权限信息
    @Override
    public String getUserAuthorityInfo(SysUser sysUser) {
        // 要返回的权限信息，格式为：ROLE_admin,ROLE_normal,sys:user:list,....
        StringBuilder authority = new StringBuilder();

        // 1、获得用户的所有角色
        List<SysRole> sysRoleList = sysRoleService.listByUserId(sysUser.getId());    // 通过用户 id 查询
        // 2、如果角色个数大于零，则将角色编码添加到 authority 中，格式为：ROLE_admin,ROLE_normal
        if (!sysRoleList.isEmpty()) {
            authority.append(
                    // 使用 stream 流的方式添加
                    sysRoleList.stream()
                            .map(sysRole -> "ROLE_" + sysRole.getCode())
                            .collect(Collectors.joining(","))
            );
        }

        // 3、获得用户所有角色关联的菜单操作（注：不同角色可能有相同的菜单操作，查询时需要使用 DISTINCT 关键字去重）
        List<SysMenu> sysMenuList = sysMenuService.listByUserId(sysUser.getId());    // 通过用户 id 查询
        // 4、如果菜单操作个数大于零，则将权限编码添加到 authority 中，格式为：sys:user:list,....
        if (!sysMenuList.isEmpty()) {
            // 和前面的角色编码用 “,” 隔开
            if (authority.length() > 0) {
                authority.append(",");
            }
            // 添加
            authority.append(
                    // 使用 stream 流的方式添加
                    sysMenuList.stream()
                            .map(SysMenu::getPerms)
                            .collect(Collectors.joining(","))
            );
        }


        return authority.toString();
    }

}




