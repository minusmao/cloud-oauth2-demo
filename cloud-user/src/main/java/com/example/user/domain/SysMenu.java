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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 菜单表
 *
 * @author minus
 * @since 2023-09-12 21:14
 */
@TableName(value = "sys_menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "菜单表")
public class SysMenu implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "上级id，一级菜单为0")
    @NotNull(message = "上级菜单不能为空")
    private Long parentId;

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "菜单名称不能为空")
    private String name;

    @ApiModelProperty(value = "URL路径")
    private String path;

    @ApiModelProperty(value = "授权(多个用逗号分隔，如：user:list,user:create)")
    @NotBlank(message = "菜单授权码不能为空")
    private String perms;

    @ApiModelProperty(value = "前端动态路由信息：视图的位置")
    private String component;

    @ApiModelProperty(value = "类型（0-目录 1-菜单 2-按钮）")
    @NotNull(message = "菜单类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "排序")
    private Integer orderNum;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除（0-否 1-是）")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "子菜单")
    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();

}