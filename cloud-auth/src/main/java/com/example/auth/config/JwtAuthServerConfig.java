package com.example.auth.config;

import com.example.common.constant.JwtConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

/**
 * 授权服务器<br>
 * 客户端 id 和 secret 配置在数据库中（见 55 行）<br>
 * 生成的 token 令牌为 jwt，其自身已经包含用户信息，授权服务器不会保存该令牌<br>
 * 可以参考：<a href="https://mp.weixin.qq.com/s/xEIWTduDqQuGL7lfiP735w">想让 OAuth2 和 JWT 在一起愉快玩耍</a>
 * <br>
 *
 * @author minus
 * @since 2023-09-08 00:50
 */
@Configuration
@EnableAuthorizationServer
public class JwtAuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${auth.jwt-signing-key}")
    private String jwtSigningKey = JwtConst.DEFAULT_JWT_SIGNING_KEY;

    /**
     * 注入访问客户端信息的业务类（数据库方式）<br>
     * 须在数据库执行 /doc/sql/oauth_client_details.sql 文件<br>
     * 该文件插入了一条客户端信息（client_id 为 client，client_secret 为 secret）
     *
     * @return ClientDetailsService
     */
    @Bean
    public ClientDetailsService clientDetails() {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        return jdbcClientDetailsService;
    }

    /**
     * 配置访问客户端信息的业务类
     *
     * @param clients the client details configurer
     * @throws Exception 异常
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }

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
     * 配置授权服务器端点
     *
     * @param endpoints the endpoints configurer
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore())                          // 令牌存储方案
                .accessTokenConverter(jwtAccessTokenConverter())    // 令牌转换器为 jwt 转换器
                .authenticationManager(authenticationManager)       // 认证管理器
                .userDetailsService(userDetailsService);            // 如果不添加，则 refresh token 的时候会报错
    }

}
