package com.joinjoy.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LikeFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String contextPath = request.getContextPath();
        String url = request.getRequestURL().toString();
        if (url.equals(contextPath + "/like")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                // 已登入，可以在這裡執行相應的邏輯
                filterChain.doFilter(request, response);
            } else {
                // 未登入，可以在這裡處理未登入的情況
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "請先登入");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
