package com.example.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.constant.ApiConst;
import com.example.common.constant.Const;
import com.example.common.exception.BadRequestException;
import com.example.user.domain.SysUser;
import com.example.user.domain.SysUserRole;
import com.example.user.model.PasswordDTO;
import com.example.user.service.SysRoleService;
import com.example.user.service.SysUserService;
import com.example.user.service.SysUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
@Api(tags = "用户相关接口")
@RestController
@RequestMapping(ApiConst.CLOUD_USER_API + "/sys/user")
public class SysUserController {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @ApiOperation("根据 id 响应某个 sys_user 的详细信息")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public SysUser info(@PathVariable("id") Long id) {
        // 查询
        SysUser sysUser = sysUserService.getById(id);
        Assert.notNull(sysUser, "找不到该管理员");

        // 得到该用户拥有的 SysRole 对象
        sysUser.setSysRoles(sysRoleService.listByUserId(id));

        return sysUser;
    }

    @ApiOperation("根据传入的用户名 name （可能为空）和分页信息，响应 sys_user 列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Page<SysUser> list(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Long size,
            @ApiParam("用户名（模糊查询）") @RequestParam(required = false) String username
    ) {
        // 查询
        Page<SysUser> sysUserPage = sysUserService.page(
                new Page<>(current, size),
                new QueryWrapper<SysUser>().like(StringUtils.isNotBlank(username), "username", username)
        );
        // 得到每个用户拥有的 SysRole 对象
        sysUserPage.getRecords().forEach(sysUser -> sysUser.setSysRoles(sysRoleService.listByUserId(sysUser.getId())));

        return sysUserPage;
    }

    @ApiOperation("响应新增 sys_user 的请求")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:user:save')")
    public SysUser save(@Validated @RequestBody SysUser sysUser) {
        // 判断用户名是否已存在
        if (sysUserService.getUserByName(sysUser.getUsername()) != null) {
            throw new BadRequestException("该用户名已存在");
        }
        // 设置信息
        sysUser.setAvatar(Const.DEFAULT_AVATAR);
        sysUser.setPassword(passwordEncoder.encode(Const.DEFAULT_PASSWORD));
        // 保存到数据库
        sysUserService.save(sysUser);

        return sysUser;
    }

    @ApiOperation("响应更新 sys_user 的请求")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public SysUser update(@Validated @RequestBody SysUser sysUser) {
        // 判断用户名是否已存在，如果用户更新了用户名，不能与其他用户相同
        SysUser anotherSysUser = sysUserService.getUserByName(sysUser.getUsername());
        if (anotherSysUser != null) {
            // 如果根据用户名查出的 id 与本身 id 不同，说明该用户名已被其他用户使用
            if (!anotherSysUser.getId().equals(sysUser.getId())) {
                throw new BadRequestException("该用户名已存在");
            }
        }
        // 更新到数据库
        sysUserService.updateById(sysUser);

        return sysUser;
    }

    @ApiOperation("响应删除 sys_user 的请求（传入 ids 数组，批量删除）")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @Transactional
    public void delete(@RequestBody Long[] ids) {
        List<Long> idList = Arrays.asList(ids);
        // 删除
        sysUserService.removeByIds(idList);
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", idList));
    }

    @ApiOperation("响应给用户分配角色的请求")
    @PostMapping("/role/{userId}")
    @PreAuthorize("hasAuthority('sys:user:role')")
    @Transactional
    public void role(@PathVariable Long userId, @RequestBody Long[] roleIds) {
        // 创建 SysUserRole 集合
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        // 根据 roleIds 和 suerId 创建 SysUserRole 对象并存入集合
        Arrays.asList(roleIds).forEach(roleId -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);

            sysUserRoleList.add(sysUserRole);
        });

        // 删除 sys_user_role 表中与 userId 关联的所有数据
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", userId));

        // 将新的角色数据放入 sys_user_role 表中
        sysUserRoleService.saveBatch(sysUserRoleList);
    }

    @ApiOperation("根据传入的 id 重置该用户的密码 888888")
    @PostMapping("/repass")
    @PreAuthorize("hasAuthority('sys:user:repass')")
    public void repass(@RequestBody Long id) {
        // 根据 id 得到 SysUser 对象
        SysUser sysUser = sysUserService.getById(id);
        // 重置密码
        sysUser.setPassword(passwordEncoder.encode(Const.DEFAULT_PASSWORD));    // 默认密码 888888
        sysUserService.updateById(sysUser);
    }

    @ApiOperation("响应更新密码的请求")
    @PostMapping("updatePass")
    public void updatePass(@Validated @RequestBody PasswordDTO passwordDTO, Principal principal) {
        // 通过用户名获取当前登陆的用户
        SysUser sysUser = sysUserService.getUserByName(principal.getName());
        // 校验旧密码是否正确
        boolean matches = passwordEncoder.matches(passwordDTO.getCurrentPass(), sysUser.getPassword());
        if (!matches) {
            throw new BadRequestException("旧密码错误");
        }

        // 更新密码
        sysUser.setPassword(passwordEncoder.encode(passwordDTO.getPassword()));
        sysUserService.updateById(sysUser);
    }

}
