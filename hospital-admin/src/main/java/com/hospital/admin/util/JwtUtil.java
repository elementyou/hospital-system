package com.hospital.admin.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    // 拿到你 application.yml 里配的密钥
    @Value("${hospital.jwt.secret}")
    private String secret;

    // 生成 Token 的方法
    public String generateToken(String username) {
        // 密钥转换
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(username) // 设置用户名
                .issuedAt(new Date()) // 签发时间
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 过期时间（这里设置24小时）
                .signWith(key) // 签名
                .compact();
    }

    // 从 Token 中解析出用户名
    public String getUsernameFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
}