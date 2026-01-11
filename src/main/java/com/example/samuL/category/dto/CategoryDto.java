package com.example.samuL.category.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "카테고리 목록 조회용 dto")
@Data
public class CategoryDto {
    @Schema(description = "카테고리 id", example = "1")
    private Short id;
    @Schema(description = "카테고리 이름", example = "동물약국")
    private String name;
    @Schema(description = "db 정렬용/카테고리 id와 동일", example = "1")
    private int sort_order;
}
