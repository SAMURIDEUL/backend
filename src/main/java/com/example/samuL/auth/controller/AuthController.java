package com.example.samuL.auth.controller;



import com.example.samuL.auth.dto.LoginRequestDto;
import com.example.samuL.auth.dto.LoginResponseDto;
import com.example.samuL.auth.dto.TokenResponseDto;
import com.example.samuL.auth.jwt.JwtTokenProvider;
import com.example.samuL.auth.service.AuthService;
import com.example.samuL.common.okResponse.OkResponse;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "로그인" , description = "이메일, 패스워드를 통해 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<OkResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto
            , HttpServletRequest request){
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
        String path = request.getRequestURI();
        return ResponseEntity.ok(OkResponse.success("로그인 성공", loginResponseDto, path));
    }




    //refresh token을 통한 access token 재발급
    @Operation(summary = "refreshToken 재발급", description = "기존의 refreshToken을 통해 새로운 accessToken을 재발급합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<OkResponse<TokenResponseDto>> refreshAccessToken(@RequestHeader("Authorization") String bearerToken
            , HttpServletRequest request){
        String refreshToken = bearerToken.replace("Bearer ", "");
        TokenResponseDto tokenResponseDto = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(OkResponse.success("access 토큰 재발급 성공", tokenResponseDto, request.getRequestURI()));
    }

    @Operation(summary = "로그아웃", description = "로그아웃 합니다.")
    @PostMapping("/logout")
    public ResponseEntity<OkResponse<Void>> logout(HttpServletRequest request, Authentication authentication){
        Map<String, String> response = new HashMap<>();
        String token = jwtTokenProvider.resolveToken(request);
        String currentEmail = authentication.getName();
        authService.logout(token, currentEmail);


        return ResponseEntity.ok(OkResponse.success("로그아웃 성공", request.getRequestURI()));
    }


}
