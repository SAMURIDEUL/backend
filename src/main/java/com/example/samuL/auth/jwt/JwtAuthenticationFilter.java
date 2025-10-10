package com.example.samuL.auth.jwt;

import com.example.samuL.common.exception.JwtAuthenticationEntryPoint;
import com.example.samuL.common.exception.JwtAuthenticationException;
import com.example.samuL.mapper.UserMapper;
import com.example.samuL.service.CustomUserDetailsService;
import com.example.samuL.service.JwtBlacklistService;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                throw new JwtAuthenticationException("토큰이 존재하지 않습니다");
//            }
            if (jwtBlacklistService.isTokenBlacklisted(token)) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("This token is blacklist token.");
//            return;
                throw new JwtAuthenticationException("This token is blacklisted");
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
        }
    }



//        if(token != null) {

//            if (jwtBlacklistService.isTokenBlacklisted(token)) {
////                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////                response.getWriter().write("This token is blacklist token.");
////                return;
//                throw new JwtAuthenticationException("This token is blacklisted");
//            }

//        try {
//            if (token != null && jwtTokenProvider.validateToken(token)) {
//                String email = jwtTokenProvider.extractEmail(token);
//                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
//
//                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                            userDetails, null, userDetails.getAuthorities());
//
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                }
//
//            }
//            filterChain.doFilter(request, response);
//
//
//        } catch (MalformedJwtException e) {
//            SecurityContextHolder.clearContext();
//            throw new JwtAuthenticationException("Malformed jwtException from filter", e);
//        } catch (IllegalArgumentException e) {
//            SecurityContextHolder.clearContext();
//            throw new JwtAuthenticationException("Illegal from filter", e);
//        }
//    }


    // filterChain.doFilter(request, response);

    //}
}