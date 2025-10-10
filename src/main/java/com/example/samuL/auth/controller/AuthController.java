package com.example.samuL.auth.controller;



import com.example.samuL.auth.dto.LoginRequestDto;
import com.example.samuL.auth.dto.LoginResponseDto;
import com.example.samuL.auth.dto.TokenResponseDto;
import com.example.samuL.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;

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






}
