package com.example.samuL.user.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Schema(description = "사용자 id")
    private Long id;
    @Schema(description = "사용자 이메일", example = "1234@example.com")
    private String email;
    @Schema(description = "사용자 패스워드", example = "12345")
    private String password_hash;
    @Schema(description = "사용자 닉네임", example = "사물이")
    private String nickname;
    @Schema(description = "계정 생성 시간")
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss", timezone = "Asia/Seoul")
    private LocalDateTime created_at;
    @Schema(description = "계정 수정 시간")
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss", timezone = "Asia/Seoul")
    private LocalDateTime updated_at;
}
