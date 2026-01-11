package com.example.samuL.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "리뷰 반환용 dto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    @Schema(description = "리뷰 id", example = "1")
    private Long id;
    @Schema(description = "장소 id", example = "23849")
    private Long placeId;
    @Schema(description = "별점", example = "5")
    private int rating;
    @Schema(description = "리뷰 내용", example = "음식이 맛있어요!")
    private String content;
    @Schema(description = "방문 날짜", example = "2025-10-19 00:00:00")
    private LocalDate visitDate;
    @Schema(description = "리뷰 생성 시간", example = "2025-11-15T22:25:44")
    private LocalDateTime createdAt;
    @Schema(description = "리뷰 업데이트 시간", example = "2025-11-16T18:37:44")
    private LocalDateTime updatedAt;
    @Schema(description = "사진 저장 위치", example = "/uploads/review_images/7d5d4c8f-1a72-4167-a348-afbdb3fe15e7.jpg")
    private List<String> photoUrls;
}
