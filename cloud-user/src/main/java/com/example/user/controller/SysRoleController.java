package com.example.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.constant.ApiConst;
import com.example.user.domain.SysRole;
import com.example.user.domain.SysRoleMenu;
import com.example.user.domain.SysUserRole;
import com.example.user.service.SysRoleMenuService;
import com.example.user.service.SysRoleService;
import com.example.user.service.SysUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
@Api(value = "角色相关接口")
@RestController
@RequestMapping(ApiConst.CLOUD_USER_API + "/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @ApiOperation("根据 id 响应某个 sys_role 的详细信息")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public SysRole info(@PathVariable("id") Long id) {
        // 根据 id 查询
        SysRole sysRole = sysRoleService.getById(id);

        /* 得到该角色拥有的 menuId */
        // 查询出该 role_id 对应的 SysRoleMenu 对象
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("role_id", id));
        // 取出 SysRoleMenu 对象的 menuId 放入 sysRole 的 menuIds
        sysRoleMenuList.stream()
                .map(SysRoleMenu::getMenuId)
                .forEach(menuId -> sysRole.getMenuIds().add(menuId));

        return sysRole;
    }

    @ApiOperation("根据传入的用户名 name （可能为空）和分页信息（由 this.getPage() 方法统一处理），响应 sys_role 列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Page<SysRole> list(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Long size,
            @ApiParam("名称（模糊查询）") @RequestParam(required = false) String name
    ) {
        return sysRoleService.page(
                new Page<>(current, size),
                new QueryWrapper<SysRole>().like(StringUtils.isNotBlank(name), "name", name)
        );
    }

    @ApiOperation("响应新增 sys_role 的请求")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:role:save')")
    public SysRole save(@Validated @RequestBody SysRole sysRole) {
        // 保存到数据库
        sysRoleService.save(sysRole);

        return sysRole;
    }

    @ApiOperation("响应更新 sys_role 的请求")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public SysRole update(@Validated @RequestBody SysRole sysRole) {
        // 保存到数据库
        sysRoleService.updateById(sysRole);

        return sysRole;
    }

    @ApiOperation("根据传入的 id 数组，响应删除 sys_role 的请求")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @Transactional
    public void delete(@RequestBody Long[] ids) {
        List<Long> idList = Arrays.asList(ids);

        // 删除
        sysRoleService.removeByIds(idList);    // 表 sys_role
        // 删除中间表
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().in("role_id", idList));
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id", idList));
    }

    @ApiOperation("响应给角色分配权限")
    @PostMapping("/perm/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:perm')")
    @Transactional    // 增加事务功能
    public void perm(@PathVariable("roleId") Long roleId, @RequestBody Long[] menuIds) {
        // 定义 SysRoleMenu 对象集合
        List<SysRoleMenu> sysRoleMenuList = new ArrayList<>();
        // 根据 menuIds 创建多个 SysRoleMenu 对象，并放入集合
        Arrays.stream(menuIds).forEach(menuId -> {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);

            sysRoleMenuList.add(sysRoleMenu);
        });

        // 在 sys_role_menu 表中删除与 roleId 关联的所有权限数据
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId));

        // 将新的权限数据存入数据库
        sysRoleMenuService.saveBatch(sysRoleMenuList);
    }

}
