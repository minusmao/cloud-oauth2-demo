package com.example.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.constant.ApiConst;
import com.example.common.exception.OperationFailureException;
import com.example.user.domain.SysMenu;
import com.example.user.domain.SysRoleMenu;
import com.example.user.model.SysMenuDTO;
import com.example.user.service.SysMenuService;
import com.example.user.service.SysRoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
@Api(value = "菜单相关接口")
@RestController
@RequestMapping(ApiConst.CLOUD_USER_API + "/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    // 响应前端所需的左侧导航的信息（包括菜单、路由、权限等）
    @RequestMapping("/nav")
    public Map<String, Object> nav() {
        // 获得权限信息
        List<String> authorities = new ArrayList<>();
        SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .forEach(a -> authorities.add(a.getAuthority()));

        // 获得导航栏信息，借助 SysMenuDto 对象
        List<SysMenuDTO> navs = sysMenuService.getCurrentUserNav();

        Map<String, Object> result = new HashMap<>();
        result.put("authorities", authorities);    // 权限信息
        result.put("nav", navs);                   // 导航信息
        return result;
    }

    @ApiOperation("根据 id 响应某个 sys_menu 的详细信息")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public SysMenu info(@PathVariable(name = "id") Long id) {
        return sysMenuService.getById(id);
    }

    @ApiOperation("响应所有 sys_menu（注意需要根据父子关系返回树状结构）")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public List<SysMenu> list() {
        return sysMenuService.tree();
    }

    @ApiOperation("响应新增 sys_menu 的请求")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:menu:save')")
    public SysMenu save(@Validated @RequestBody SysMenu sysMenu) {
        // 保存到数据库
        sysMenuService.save(sysMenu);

        return sysMenu;
    }

    @ApiOperation("响应更新 sys_menu 的请求")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public SysMenu update(@Validated @RequestBody SysMenu sysMenu) {
        // 更新数据库
        sysMenuService.updateById(sysMenu);

        return sysMenu;
    }

    @ApiOperation("根据传入的 id 响应删除 sys_menu 的请求")
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    @Transactional
    public void delete(@PathVariable Long id) {
        // 查看子菜单的个数
        int count = sysMenuService.count(new QueryWrapper<SysMenu>().eq("parent_id", id));
        if (count > 0) {
            // 如果个数不为零，则响应提示
            throw new OperationFailureException("请先删除子菜单");
        }

        // 数据库删除
        sysMenuService.removeById(id);
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("menu_id", id));
    }

}
