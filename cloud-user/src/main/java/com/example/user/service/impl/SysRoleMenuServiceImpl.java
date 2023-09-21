package com.example.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.domain.SysRoleMenu;
import com.example.user.service.SysRoleMenuService;
import com.example.user.mapper.SysRoleMenuMapper;
import org.springframework.stereotype.Service;

/**
 * 针对表【sys_role_menu(角色和菜单的关系表)】的数据库操作Service实现
 *
 * @author minus
 * @since 2023-09-12 21:03:22
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

}




