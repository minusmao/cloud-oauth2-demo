package com.example.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 更新密码时，所用的 dto 数据对象
 *
 * @author minus
 * @since 2023-09-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "更新密码时，所用的 dto 数据对象")
public class PasswordDTO {

    @ApiModelProperty(value = "新密码")
    @NotBlank(message = "新密码不能为空")
    private String password;

    @ApiModelProperty(value = "确认密码")
    private String checkPass;

    @ApiModelProperty(value = "旧密码")
    @NotBlank(message = "旧密码不能为空")
    private String currentPass;

}
