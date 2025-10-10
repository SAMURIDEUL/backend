package com.example.samuL.auth.controller;



import com.example.samuL.auth.dto.LoginRequestDto;
import com.example.samuL.auth.dto.LoginResponseDto;
import com.example.samuL.auth.dto.TokenResponseDto;
import com.example.samuL.auth.jwt.JwtTokenProvider;
import com.example.samuL.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
        return ResponseEntity.ok(loginResponseDto);
    }

    //refresh token을 통한 access token 재발급
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refreshAccessToken(@RequestHeader("Authorization") String bearerToken){
        String refreshToken = bearerToken.replace("Bearer ", "");
        TokenResponseDto tokenResponseDto = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(tokenResponseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, Authentication authentication){
        Map<String, String> response = new HashMap<>();
        String token = jwtTokenProvider.resolveToken(request);
        String currentEmail = authentication.getName();
        authService.logout(token, currentEmail);
        response.put("message", "로그아웃 성공");
        return ResponseEntity.ok(response);
    }


}
