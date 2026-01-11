package com.example.samuL.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "리뷰 사진 전용 dto")
@Data
public class ReviewPhotoDto {
    @Schema(description = "사진 id", example = "1")
    private Long id;
    @Schema(description = "리뷰 id", example = "10")
    private Long reviewId;
    @Schema(description = "사진 저장 장소", example = "/uploads/review_images/7d5d4c8f-1a72-4167-a348-afbdb3fe15e7.jpg")
    private String photoUrl;
}
