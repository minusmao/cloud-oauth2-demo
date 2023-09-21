package com.example.user.mapper;

import com.example.user.domain.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 针对表【sys_role(角色表)】的数据库操作Mapper
 *
 * @author minus
 * @since 2023-09-12 21:00:39
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户 id 查询该用户的所有角色
     *
     * @param userId 用户 id
     * @return 角色列表
     */
    List<SysRole> selectByUserId(Long userId);

}




