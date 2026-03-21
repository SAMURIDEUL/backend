package com.example.samuL.auth.jwt;

import com.example.samuL.common.exception.jwtAuth.JwtAuthenticationEntryPoint;
import com.example.samuL.common.exception.jwtAuth.JwtAuthenticationException;
import com.example.samuL.user.service.CustomUserDetailsService;
import com.example.samuL.JwtBlackList.service.JwtBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtBlacklistService jwtBlacklistService;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    // 인증이 필요없는 api
    private static final List<String> WHITELIST = List.of(
            "/users/login", "/users/signup", "/users/check-email", "/users/check-nickname", "/categories",
            "/categories/**", "/places", "/places/**", "/places/random", "/place", "/place/**", "/uploads/**", 
            "/swagger-ui.html", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**");
    // 인증이 필요한 api, 인증이 필요없는 api와 겹친 경우 해결하기 어려워 추가
    private static final List<String> jwt_required = List.of(
            "/places/*/like", "/places/likes");

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // OPTIONS 요청 방어 (Preflight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        String requestURI = request.getRequestURI() == null ? "" : request.getRequestURI();

        boolean isJwtRequired = false;
        for (String pattern : jwt_required) {
            if (pattern != null && pathMatcher.match(pattern, requestURI)) {
                isJwtRequired = true;
                break;
            }
        }

        // 토큰이 없는 경우
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // GET 요청이면서 리뷰/장소 조회인 경우 패스
            boolean isPublicGet = "GET".equalsIgnoreCase(request.getMethod()) && 
                    (pathMatcher.match("/place/**", requestURI) || pathMatcher.match("/places/**", requestURI));

            if ((isWhitelisted(requestURI) && !isJwtRequired) || isPublicGet) {
                filterChain.doFilter(request, response);
                return;
            } else {
                SecurityContextHolder.clearContext();
                jwtAuthenticationEntryPoint.commence(request, response, new JwtAuthenticationException("토큰이 없습니다."));
                return;
            }
        }

        // 토큰이 있는 경우 무조건 검증 시도
        try {
            String token = authHeader.substring(7);

            if (jwtBlacklistService.isTokenBlacklisted(token)) {
                throw new JwtAuthenticationException("블랙리스트에 등록된 토큰입니다.");
            }

            jwtTokenProvider.validateToken(token);

            String email = jwtTokenProvider.extractEmail(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            jwtAuthenticationEntryPoint.commence(request, response, ex);
        }

    }

    private boolean isWhitelisted(@NonNull String ur) {
        for (String pattern : WHITELIST) {
            if (pattern != null && pathMatcher.match(pattern, ur)) {
                return true;
            }
        }
        return false;
    }
}
