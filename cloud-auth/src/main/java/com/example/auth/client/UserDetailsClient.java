package com.example.auth.client;

import com.example.common.domain.AccountUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户信息访问客户端（远程调用 CLOUD_USER 服务）
 *
 * @author minus
 * @since 2023/9/21 13:42
 */
@FeignClient(name = "CLOUD-USER")
public interface UserDetailsClient {

    @GetMapping("/auth/user/details")
    AccountUser loadUserByUsername(@RequestParam String username) throws UsernameNotFoundException;

}
