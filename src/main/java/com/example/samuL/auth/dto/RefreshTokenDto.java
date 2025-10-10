package com.example.samuL.auth.dto;


import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class RefreshTokenDto {
    private BigInteger id;
    private String email;
    private String refreshToken;
    private LocalDateTime expired_at;
    private LocalDateTime created_at;
}
