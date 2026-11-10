package com.hospital.admin.config;

import com.hospital.admin.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 从请求头拿 Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. 如果请求头没带 Bearer Token，直接放行（交给后面的安全规则去拦截）
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 提取 Token (去掉 "Bearer " 这 7 个字符)
        String token = authHeader.substring(7);

        try {
            // 4. 解析用户名
            String username = jwtUtil.getUsernameFromToken(token);

            // 5. 如果解析出了用户名，且当前上下文没有认证信息
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 去数据库（或者你的内存配置里）查用户信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 6. 构造认证对象并存入 SecurityContextHolder
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Token 过期或伪造，这里可以打个日志，不用抛异常，直接放行让 Security 去处理无权限
            System.out.println("Token 解析失败: " + e.getMessage());
        }

        // 7. 继续执行后面的过滤器
        filterChain.doFilter(request, response);
    }
}