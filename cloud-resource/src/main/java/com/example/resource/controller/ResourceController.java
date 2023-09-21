package com.example.resource.controller;

import com.example.common.constant.ApiConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资源控制器
 * @author minus
 * @since 2023/9/21 15:47
 */
@Api(tags = "资源控制器")
@RestController
@RequestMapping(ApiConst.CLOUD_RESOURCE_API)
public class ResourceController {

    @ApiOperation("测试")
    @GetMapping("/test")
    @PreAuthorize("hasAuthority('resource:test')")
    public String test() {
        return "ok";
    }

}
