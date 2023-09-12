package com.example.user.config;

import com.example.common.constant.JwtConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 资源服务器<br>
 *
 * @author minus
 * @since 2023-09-08 00:50
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class JwtResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${auth.jwt-signing-key}")
    private String jwtSigningKey = JwtConst.DEFAULT_JWT_SIGNING_KEY;

    /**
     * 注入令牌存储方案，因为这里使用 jwt 方案，所以实际没有存储令牌
     *
     * @return TokenStore
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 配置并注入 jwt 转换器
     *
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(jwtSigningKey);// 对称密钥
        return converter;
    }

    /**
     * 资源服务器相关配置
     *
     * @param resources configurer for the resource server
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        // 令牌存储方案
        resources.tokenStore(tokenStore());
    }

    /**
     * 配置资源服务器的安全策略
     *
     * @param http the current http filter configuration
     * @throws Exception 异常
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/login", "/user/register").permitAll()
                .antMatchers("/**").authenticated();
    }

}
