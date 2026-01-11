package com.example.samuL.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "비밀번호 변경 update용 dto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordUpdateDto {
    @Schema(description = "사용자 email", example = "1234@example.com")
    private String email;
    @Schema(description = "새로운 비밀번호", example = "5678")
    private String newPassword;
}
