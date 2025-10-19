package com.example.samuL.common.okResponse;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OkResponse<T> {
    private int status;
    private String message;
    private T data;
    private String timestamp;
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
