package com.example.samuL.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "리뷰 페이지네이션 전용 dto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewPaginatedResponse<T>{
    @Schema(description = "실제 데이터들")
    private List<T> content; // 실제 데이터
    @Schema(description = "현재 페이지", example = "0")
    private int page; // 현재 페이지
    @Schema(description = "페이지 크기/한 번에 가져올 수 있는 리뷰 수", example = "20")
    private int size; // 페이지 크기
    @Schema(description = "총 리뷰 개수", example = "2")
    private long totalElements; // 전체 수
    @Schema(description = "전체 페이지 수", example = "1")
    private int totalPages; // 전체 페이지 수
}
