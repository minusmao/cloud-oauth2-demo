package com.example.user.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 用户表
 *
 * @author minus
 * @since 2023-09-12 21:14
 */
@TableName(value ="sys_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户表")
public class SysUser implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "头像的URL")
    private String avatar;

    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "上次登录时间")
    @JsonFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private LocalDateTime lastLogin;

    @ApiModelProperty(value = "是否删除（0-否 1-是）")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "拥有的角色 SysRole 对象")
    @TableField(exist = false)
    private List<SysRole> sysRoles = new ArrayList<>();

}