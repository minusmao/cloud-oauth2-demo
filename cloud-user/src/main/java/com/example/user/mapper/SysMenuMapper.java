package com.example.user.mapper;

import com.example.user.domain.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 针对表【sys_menu(菜单表)】的数据库操作Mapper
 *
 * @author minus
 * @since 2023-09-12 20:59:28
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据用户 id 查询该用户的所有角色
     * @param userId 用户 id
     */
    List<SysMenu> selectByUserId(@Param("userId") Long userId);

}




