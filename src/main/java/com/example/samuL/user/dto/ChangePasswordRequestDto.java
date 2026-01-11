package com.example.samuL.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "비밀번호 변경 request용 dto")
@Data
public class ChangePasswordRequestDto {
    @Schema(description = "현재 비밀번호", example = "1234")
    private String currentPassword;
    @Schema(description = "새로운 비밀번호", example = "5678")
    private String newPassword;
}
