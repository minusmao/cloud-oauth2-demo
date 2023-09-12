package com.example.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 认证服务返回的 token 信息
 *
 * @author minus
 * @since 2023/9/12 14:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("认证 token 信息")
public class OAuthTokenDTO {

    @ApiModelProperty("访问令牌")
    @JsonProperty("access_token")
    private String accessToken;

    @ApiModelProperty("令牌类型")
    @JsonProperty("token_type")
    private String tokenType;

    @ApiModelProperty("刷新令牌")
    @JsonProperty("refresh_token")
    private String refreshToken;

    @ApiModelProperty("过期时间")
    @JsonProperty("expires_in")
    private int expiresIn;

    @ApiModelProperty("作用域")
    private String scope;

    @ApiModelProperty("用户标识")
    private String jti;

}
