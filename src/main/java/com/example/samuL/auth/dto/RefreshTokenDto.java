package com.example.samuL.auth.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RefreshTokenDto {
    private Long id;
    private String email;
    private String refreshToken;
    private String lastAccessToken;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;
}
