package com.example.samuL.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "랜덤 6개 장소의 장소+썸네일용 dto")
@Data
@AllArgsConstructor
public class RandomPlaceResponse {
    @Schema(description = "장소 + 반려동물 제약사항")
    private PlacePlaceDto places;
    @Schema(description = "썸네일용 사진 1개", example = "/uploads/review_images/7d5d4c8f-1a72-4167-a348-afbdb3fe15e7.jpg")
    private String thumbnail;
}
