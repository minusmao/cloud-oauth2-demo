package com.example.user.controller;

import com.example.common.domain.AccountUser;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息接口
 * @author minus
 * @since 2023/9/21 14:49
 */
@RestController
@RequestMapping("/auth/user")
public class UserDetailsController {

    @Autowired
    private UserDetailsService userDetailsService;

    @ApiOperation("用户信息（此接口只用于 CLOUD_AUTH 服务访问，不宜暴露到外部）")
    @GetMapping("/details")
    public AccountUser loadUserByUsername(@RequestParam String username) {
        return (AccountUser) userDetailsService.loadUserByUsername(username);
    }

}
