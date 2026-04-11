package com.example.samuL.place.dto;

import com.example.samuL.review.dto.ReviewPhotoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Schema(description = "장소에대한 리뷰 조회용 dto")
@Data
public class PlaceReviewDto {
    @Schema(description = "리뷰 id", example = "1")
    private Long id;
    @Schema(description = "장소 id", example = "23849")
    private Long placeId;
    @Schema(description = "사용자 id", example = "8")
    private Long userId;
    @Schema(description = "사용자 닉네임", example = "사물이")
    private String nickname;
    @Schema(description = "별점", example = "5")
    private float rating;
    @Schema(description = "리뷰 내용", example = "음식이 맛있어요!")
    private String content;
    @Schema(description = "방문 날짜", example = "2026-01-10T00:00:00")
    private LocalDateTime visitDate;
    @Schema(description = "리뷰 생성 시각", example = "2026-01-10T15:14:15")
    private LocalDateTime createdAt;
    @Schema(description = "사진 목록 (id + photoUrl)")
    private List<ReviewPhotoDto> photos;
}
