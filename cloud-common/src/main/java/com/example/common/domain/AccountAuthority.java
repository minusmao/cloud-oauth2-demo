package com.example.common.domain;

import io.swagger.annotations.ApiModel;
import org.springframework.security.core.GrantedAuthority;

/**
 * 权限信息
 *
 * @author minus
 * @since 2023/9/21 14:54
 */
@ApiModel(value = "权限信息")
public class AccountAuthority implements GrantedAuthority {

    private String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

}
