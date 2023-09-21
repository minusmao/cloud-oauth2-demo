package com.example.gateway.filters;

import com.example.common.constant.JwtConst;
import com.example.common.util.JacksonUtil;
import com.example.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 自定义 token 验证filter
 * 参考：<a href="https://blog.csdn.net/qq_46203643/article/details/127150590">自定义 gateway 网关过滤器</a>
 * 参考：<a href="https://blog.csdn.net/weixin_44894196/article/details/129296893">GateWay 网关自定义过滤器实现 token 校验</a>
 *
 * @author minus
 * @since 2023-08-30 00:04
 */
@Component
public class TokenGatewayFilterFactory extends AbstractGatewayFilterFactory<TokenGatewayFilterFactory.Config> {

    @Value("${auth.jwt-signing-key}")
    private String jwtSigningKey = JwtConst.DEFAULT_JWT_SIGNING_KEY;

    @Data
    public static class Config {
    }

    public TokenGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 判断请求头中是否有 token
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (token == null || token.isEmpty()) {
                // 没有token
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return responseData("没有token", exchange.getResponse());
            }
            // 验证 token 有效性
            String tokenJwt = token.replace("Bearer ", "");
            Claims claimsByToken = JwtUtil.getClaimsByToken(tokenJwt, jwtSigningKey);
            if (claimsByToken == null) {
                // token无效
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return responseData("token无效", exchange.getResponse());
            }
            // 验证 token 是否过期
            if (JwtUtil.isTokenExpired(claimsByToken)) {
                // token过期
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return responseData("token过期", exchange.getResponse());
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> responseData(String message, ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> data = new HashMap<>();
        data.put("message", message);
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(
                Objects.requireNonNull(JacksonUtil.beanToJsonStr(data)).getBytes(StandardCharsets.UTF_8)
        );
        return response.writeWith(Flux.just(bodyDataBuffer));
    }

}
