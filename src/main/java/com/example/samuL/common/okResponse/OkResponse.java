package com.example.samuL.common.okResponse;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "공통 API 200 ok 응답 구조")
@Data
public class OkResponse<T> {
    @Schema(description = "HTTP 상태 코드", example = "200")
    private int status;
    @Schema(description = "응답 메시지", example = "요청 성공")
    private String message;
    @Schema(description = "실제 데이터")
    private T data;
    @Schema(description = "요청 시각", example = "2026-01-10T16:41:59.359279800")
    private String timestamp;
    @Schema(description = "요청 URL", example = "/places/nearby")
    private String path;

    public OkResponse(int status, String message, T data, String path){
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now().toString();
        this.path = path;
    }

    public static <T> OkResponse<T> success(String message, String path){
        return new OkResponse<>(200, message, null, path);
    }

    public static <T> OkResponse<T> success(T data, String path){
        return new OkResponse<>(200, "요청 성공", data, path);
    }

    public static <T> OkResponse<T> success(String message, T data, String path){
        return new OkResponse<>(200, message, data, path);
    }
}
