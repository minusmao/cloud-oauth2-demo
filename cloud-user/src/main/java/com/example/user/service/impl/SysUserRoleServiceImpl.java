package com.example.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.domain.SysUserRole;
import com.example.user.service.SysUserRoleService;
import com.example.user.mapper.SysUserRoleMapper;
import org.springframework.stereotype.Service;

/**
 * 针对表【sys_user_role(用户和角色的关系表)】的数据库操作Service实现
 *
 * @author minus
 * @since 2023-09-12 21:03:39
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

}




