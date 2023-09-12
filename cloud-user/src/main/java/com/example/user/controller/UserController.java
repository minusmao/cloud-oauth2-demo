package com.example.user.controller;

import com.example.user.model.OAuthTokenDTO;
import com.example.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 用户相关接口
 * @author minus
 * @since 2023/9/12 15:41
 */
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public OAuthTokenDTO login(
            @ApiParam("用户名") @RequestParam String username,
            @ApiParam("密码") @RequestParam String password
    ) {
        return userService.login(username, password);
    }

    @ApiOperation("用户信息")
    @GetMapping("/info")
    public Authentication info() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
