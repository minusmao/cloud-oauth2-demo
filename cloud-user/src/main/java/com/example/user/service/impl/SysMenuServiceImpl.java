package com.example.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.domain.SysMenu;
import com.example.user.domain.SysUser;
import com.example.user.model.SysMenuDTO;
import com.example.user.service.SysMenuService;
import com.example.user.mapper.SysMenuMapper;
import com.example.user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 针对表【sys_menu(菜单表)】的数据库操作Service实现
 *
 * @author minus
 * @since 2023-09-12 20:59:28
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    
    @Autowired
    private SysUserService sysUserService;     // 注入 SysUserService 层

    // 获得用户所有角色关联的菜单操作（注：不同角色可能有相同的菜单操作，查询时需要使用 DISTINCT 关键字去重）
    @Override
    public List<SysMenu> listByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }
    
    @Override
    public List<SysMenuDTO> getCurrentUserNav() {
        // 从 security 的上下文中获取当前用户
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserService.getUserByName(username);

        // 获得用户所有角色关联的菜单操作
        List<SysMenu> sysMenuList = this.listByUserId(sysUser.getId());

        // 转化为 List<SysMenuDTO> 并返回
        return this.convertToDto(sysMenuList);
    }

    /**
     * 将 List<SysMenu> 转化为 List<SysMenuDTO>
     */
    private List<SysMenuDTO> convertToDto(List<SysMenu> sysMenuList) {
        // 定义 DTO 集合
        List<SysMenuDTO> SysMenuDTOList = new ArrayList<>();

        /* sysMenuList 为并列关系，通过判断他们的 parentId 把他们变成父子关系（最多三代），装载到 SysMenuDTOList 中 */
        // 先全部转化为 SysMenuDTO 对象，放入 SysMenuDTOMap 中（id 作为 key）
        Map<Long, SysMenuDTO> sysMenuDTOMap = getLongSysMenuDTOMap(sysMenuList);
        // 判断父子关系，将子节点 DTO 添加到父节点 DTO 的 children 中，再将第一层（parentId 为 0）放入  SysMenuDTOList 中
        for (SysMenu sm : sysMenuList) {
            // 获得 parentId
            Long parentId = sm.getParentId();
            // 判断 parentId
            if (parentId == 0) {
                // 当 parentId 为 0 时（即第一层节点），从 SysMenuDTOMap 中提取出来放入 SysMenuDTOList
                SysMenuDTOList.add(sysMenuDTOMap.get(sm.getId()));
            } else {
                // 根据 parentId 添加到父节点的 children 中
                SysMenuDTO child = sysMenuDTOMap.get(sm.getId());        // 从 Map 中找到当前节点 DTO
                sysMenuDTOMap.get(parentId).getChildren().add(child);    // 从 Map 中找到父节点 DTO，并添加到父节点的 children 中
            }
        }

        return SysMenuDTOList;
    }

    private static Map<Long, SysMenuDTO> getLongSysMenuDTOMap(List<SysMenu> sysMenuList) {
        Map<Long, SysMenuDTO> sysMenuDTOMap = new HashMap<>();    // 临时存储所有的 DTO 对象（id 作为 key）
        for (SysMenu sm : sysMenuList) {
            // 转化为 SysMenuDTO 对象
            SysMenuDTO smd = new SysMenuDTO();
            smd.setId(sm.getId());                  // id
            smd.setName(sm.getPerms());             // 编码
            smd.setTitle(sm.getName());             // 名称
            smd.setIcon(sm.getIcon());              // 图标
            smd.setPath(sm.getPath());              // 路由
            smd.setComponent(sm.getComponent());    // 动态路由所需的 Component 信息
            // 添加到 SysMenuDTOList 中
            sysMenuDTOMap.put(sm.getId(), smd);
        }
        return sysMenuDTOMap;
    }

    @Override
    public List<SysMenu> tree() {
        // 查询所有的 menu 并按照 order_num 字段排序
        List<SysMenu> sysMenuList = this.list(new QueryWrapper<SysMenu>().orderByAsc("order_num"));

        // 转化为树状结构并返回
        return this.buildTreeMenu(sysMenuList);
    }

    private List<SysMenu> buildTreeMenu(List<SysMenu> sysMenuList) {
        // 要返回的树状结构数据
        List<SysMenu> treeMenuList = new ArrayList<>();

        // 生成树状结构
        for (SysMenu sm : sysMenuList) {
            // 找到自己的 children 并添加
            for (SysMenu m : sysMenuList) {
                if (sm.getId().equals(m.getParentId())) {
                    sm.getChildren().add(m);    // 添加子节点
                }
            }

            // 将第一层，即 parentId 为 0 的节点添加到 treeMenuList 中
            if (sm.getParentId() == 0L) {
                treeMenuList.add(sm);
            }
        }

        return treeMenuList;
    }
    
}




