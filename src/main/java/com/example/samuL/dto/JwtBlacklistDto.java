package com.example.samuL.dto;


import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class JwtBlacklistDto {
    //private Long id;
    private String token;
    private LocalDateTime expired_at;
    private LocalDateTime created_at;
}
