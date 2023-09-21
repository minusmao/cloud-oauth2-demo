package com.example.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件名：SysMenuDto.java
 * 描述：前端所需的左侧导航栏的数据对象
 * 时间：2021-07-04
 * 作者：TechRice
 * 数据形式如下：
 *     {
 * 			name: 'SysManga',
 * 			title: '系统管理',
 * 			icon: 'el-icon-s-operation',
 * 			component: '',
 * 			path: '',
 * 			children: []
 *     }
 * @author minus
 */
@ApiModel(value = "前端所需的左侧导航栏的数据对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysMenuDTO {

    @ApiModelProperty(value = "菜单 id")
    private Long id;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "菜单标题")
    private String title;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "菜单路径")
    private String path;

    @ApiModelProperty(value = "菜单组件")
    private String component;

    @ApiModelProperty(value = "子菜单列表")
    private List<SysMenuDTO> children = new ArrayList<>();

}
