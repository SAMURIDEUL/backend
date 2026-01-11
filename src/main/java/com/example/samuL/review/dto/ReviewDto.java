package com.example.samuL.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;


@Schema(description = "리뷰용 dto")
@Data
public class ReviewDto {
    @Schema(description = "리뷰 id", example = "1")
    private Long id;
    @Schema(description = "장소 id", example = "23849")
    private Long placeId;
    @Schema(description = "사용자 id", example = "8")
    private Long userId;
    @Schema(description = "별점", example = "5")
    private int rating;
    @Schema(description = "리뷰 내용", example = "음료가 따뜻해서 좋아요!")
    private String content;
    @Schema(description = "방문 날짜", example="2025-10-19 00:00:00")
    private LocalDate visitDate;
}
