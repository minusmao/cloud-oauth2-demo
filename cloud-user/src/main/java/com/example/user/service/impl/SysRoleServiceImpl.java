package com.example.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.domain.SysRole;
import com.example.user.service.SysRoleService;
import com.example.user.mapper.SysRoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 针对表【sys_role(角色表)】的数据库操作Service实现
 *
 * @author minus
 * @since 2023-09-12 21:00:39
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public List<SysRole> listByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }

}




