package com.example.samuL.place.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "장소와 썸네일용 사진 1개 반환용 dto")
@Data
public class PlaceLocDetailDto {
    @Schema(description = "장소+반려동물 제약 사항")
    private PlacePlaceDto placeInfo;
    @Schema(description = "썸네일용 사진 1장", example="/uploads/review_images/7d5d4c8f-1a72-4167-a348-afbdb3fe15e7.jpg")
    private String photo;
}
