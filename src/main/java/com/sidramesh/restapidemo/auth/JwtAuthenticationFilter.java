package com.sidramesh.restapidemo.auth;

import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;

import java.util.ArrayList;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final String cookieName;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, String cookieName) {
        this.jwtUtil = jwtUtil;
        this.cookieName = cookieName;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws java.io.IOException, jakarta.servlet.ServletException {

        try {
            String token = null;
            if (request.getCookies() != null) {
                for (Cookie c : request.getCookies()) {
                    if (cookieName.equals(c.getName())) { token = c.getValue(); break; }
                }
            }
            if (token != null && !token.isBlank()) {
                var claims = jwtUtil.validate(token);
                String userId = claims.getSubject();
                // Create simple Authentication with userId as principal; adapt to use roles if needed
                var auth = new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception ex) {
            // invalid token -> no authentication set
        }
        chain.doFilter(request, response);
    }
}