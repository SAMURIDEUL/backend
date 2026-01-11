package com.example.samuL.user.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyInfoDto {
    @Schema(description = "이메일", example = "1234@example.com")
    private String email;
    @Schema(description = "닉네임", example = "사물이")
    private String nickname;
    @Schema(description = "계정 생성 시간", example = "2025-10-21 19:56:08")
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss", timezone = "Asia/Seoul")
    private LocalDateTime created_at;
    @Schema(description = "계정 수정 시간", example = "2025-10-22 19:48:20")
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss", timezone = "Asia/Seoul")
    private LocalDateTime updated_at;
}
