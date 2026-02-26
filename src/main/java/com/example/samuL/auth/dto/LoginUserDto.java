package com.example.samuL.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDto {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;
}
