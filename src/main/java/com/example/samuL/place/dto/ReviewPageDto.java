package com.example.samuL.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "라뷰 및 페이지 기능용 dto")
@Data
public class ReviewPageDto {
    @Schema(description = "리뷰들 조회")
    private List<PlaceReviewDto> reviews;
    @Schema(description = "다음 페이지 존재 여부 확인", example = "true")
    private boolean hasNext;
}
