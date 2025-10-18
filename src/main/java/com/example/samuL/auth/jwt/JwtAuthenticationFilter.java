package com.example.samuL.auth.jwt;

import com.example.samuL.common.exception.jwtAuth.JwtAuthenticationEntryPoint;
import com.example.samuL.common.exception.jwtAuth.JwtAuthenticationException;
import com.example.samuL.user.mapper.UserMapper;
import com.example.samuL.user.service.CustomUserDetailsService;
import com.example.samuL.JwtBlackList.service.JwtBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtBlacklistService jwtBlacklistService;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserMapper userMapper;

    private static final List<String> WHITELIST = List.of(
            "/users/login", "/users/signup", "/users/check-email", "/users/check-nickname"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

//
        final String authHeader = request.getHeader("Authorization");
        // 화이트 리스트, 토큰 증명이 필요없는 경우
        String requestURI = request.getRequestURI();

        if(isWhitelisted(requestURI)){
            filterChain.doFilter(request,response);
            return;
        }
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authHeader.substring(7);

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new JwtAuthenticationException("토큰이 없습니다.");

            }

            String token = authHeader.substring(7);

            if (jwtBlacklistService.isTokenBlacklisted(token)) {
                throw new JwtAuthenticationException("블랙리스트에 등록된 토큰입니다.");
            }

            jwtTokenProvider.validateToken(token);

            String email = jwtTokenProvider.extractEmail(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            jwtAuthenticationEntryPoint.commence(request, response, ex);
         //   return;
        }

    }
    private boolean isWhitelisted(String ur){
        return WHITELIST.stream().anyMatch(pattern -> pathMatcher.match(pattern, ur));
    }
}


