package com.example.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 文件名：AccountUser.java
 * 描述：用于给 security 验证用户信息的实体类，实现了 UserDetails 接口
 * 时间：2021-07-03
 * 作者：TechRice
 * @author minus
 * @since 2023-09-21
 */
@ApiModel(value = "用户信息")
@Data
@NoArgsConstructor
public class AccountUser implements UserDetails {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户权限")
    private List<AccountAuthority> authorities;

    @ApiModelProperty(value = "用户是否过期")
    private boolean accountNonExpired;

    @ApiModelProperty(value = "用户是否锁定")
    private boolean accountNonLocked;

    @ApiModelProperty(value = "用户密码是否过期")
    private boolean credentialsNonExpired;

    @ApiModelProperty(value = "用户是否可用")
    private boolean enabled;

    public AccountUser(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(userId, username, password, true, true, true, true, authorities);
    }


    public AccountUser(Long userId, String username, String password, boolean enabled, boolean accountNonExpired,
                       boolean credentialsNonExpired, boolean accountNonLocked,
                       Collection<? extends GrantedAuthority> authorities) {
        Assert.isTrue(username != null && !username.isEmpty() && password != null,
                "Cannot pass null or empty values to constructor");
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        List<AccountAuthority> accountAuthorities = new ArrayList<>();
        authorities.forEach(authority -> {
            AccountAuthority accountAuthority = new AccountAuthority();
            accountAuthority.setAuthority(authority.getAuthority());
            accountAuthorities.add(accountAuthority);
        });
        this.authorities = accountAuthorities;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}

