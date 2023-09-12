package com.example.user.client;

import com.example.user.model.OAuthTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 访问认证服务的客户端
 *
 * @author minus
 * @since 2023/9/12 14:42
 */
@FeignClient(name = "CLOUD-AUTH")
public interface CloudAuthClient {

    /**
     * 获取认证令牌
     * @param authorization 客户端信息编码（格式："Basic " + "client_id:client_secret 的 base64 编码"）
     * @param grantType 授权模式
     * @param username 用户名
     * @param password 密码
     * @return OAuthTokenDTO
     */
    @PostMapping("/oauth/token")
    OAuthTokenDTO getToken(
            @RequestHeader("Authorization") String authorization,
            @RequestParam("grant_type") String grantType,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    );

}
