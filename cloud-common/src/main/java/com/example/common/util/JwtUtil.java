package com.example.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 的工具类
 *
 * @author minus
 * @since 2023-09-21 15:50
 */
public class JwtUtil {

    /**
     * 解析 JWT 信息，如果信息不合法返回 null
     *
     * @param jwt    JWT 数据
     * @param secret 加密密钥
     * @return JWT 信息
     */
    public static Claims getClaimsByToken(String jwt, String secret) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断 JWT 是否过期
     * @param claims JWT 信息
     * @return 是否过期
     */
    public static boolean isTokenExpired(Claims claims) {
        return claims.getExpiration()    // 获得过期时间
                .before(new Date());     // 判断是否在当前时间之前
    }
}
