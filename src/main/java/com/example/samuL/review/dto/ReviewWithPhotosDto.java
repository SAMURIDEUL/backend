package com.example.samuL.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewWithPhotosDto {
    @Schema(description = "리뷰 id")
    private Long id;
    @Schema(description = "장소 id")
    private Long placeId;
    @Schema(description = "사용자 id")
    private Long userId;
    @Schema(description = "별점", example = "3")
    private Float rating;
    @Schema(description = "리뷰 내용", example = "음식이 싱거워요")
    private String content;
    @Schema(description = "방문 날짜", example = "2025-10-19")
    private LocalDate visitDate;
    @Schema(description = "리뷰 생성 시간", example = "2025-11-15T22:25:44")
    private LocalDateTime createdAt;
    @Schema(description = "리뷰 업데이트 시간", example = "2025-11-16T18:37:44")
    private LocalDateTime updatedAt;
    @Schema(description = "사진")
    private List<ReviewPhotoDto> photos = new ArrayList<>();
}
