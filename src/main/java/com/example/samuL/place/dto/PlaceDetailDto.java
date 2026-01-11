package com.example.samuL.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "Place 및 장소에 저장된 사진 3개 조회용 dto")
@Data
public class PlaceDetailDto {
    @Schema(description = "장소 정보 + 반려동물 제한 사항")
    private PlacePlaceDto placeInfo;
    @Schema(description = "장소에 대한 사진 3개 조회", example = "[" +
            "            \"/uploads/review_images/7d5d4c8f-1a72-4167-a348-afbdb3fe15e7.jpg\"," +
            "            \"/uploads/review_images/eace0e4a-979b-464d-84e8-cd572b8b3b21.jpg\"," +
            "            \"/images/default/cafe.png\"" +
            "        ]")
    private List<String> top3photos;
}
