package com.example.user.service;

import com.example.user.domain.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.user.model.SysMenuDTO;

import java.util.List;

/**
 * 针对表【sys_menu(菜单表)】的数据库操作Service
 *
 * @author minus
 * @since 2023-09-12 20:59:28
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获得用户所有角色关联的菜单操作（注：不同角色可能有相同的菜单操作，查询时需要使用 DISTINCT 关键字去重）
     * @param userId 用户 id
     */
    List<SysMenu> listByUserId(Long userId);

    /**
     * 以树状结构返回所有的 sys_menu（树状结构根据父子关系生成，利用 SysMenu 对象中定义的 children 属性）
     */
    List<SysMenuDTO> getCurrentUserNav();

    /**
     * 查询菜单属性结构
     */
    List<SysMenu> tree();

}
