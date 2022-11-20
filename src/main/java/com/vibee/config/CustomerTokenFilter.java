package com.vibee.config;

import com.vibee.config.redis.RedisAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomerTokenFilter extends OncePerRequestFilter {
    private static final String TOKEN_PREFIX="Bearer";
    @Autowired
    private RedisAdapter redisAdapter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token=request.getHeader("Authorization");
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (token!=null && token.startsWith(TOKEN_PREFIX)){
            String accessToken=token.substring(TOKEN_PREFIX.length());
            String key="accessToken::"+accessToken.hashCode();
            if (redisAdapter.exists(key)){
                new SecurityContextLogoutHandler().logout(request,response,authentication);
                SecurityContextHolder.getContext().setAuthentication(null);
                authentication.setAuthenticated(false);
            }
        }
        filterChain.doFilter(request,response);
    }
}
