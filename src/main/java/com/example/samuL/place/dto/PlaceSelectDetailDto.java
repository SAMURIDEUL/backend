package com.example.samuL.place.dto;


import lombok.Data;

import java.util.List;

@Data
public class PlaceSelectDetailDto {
    // 장소 조회 시 사용하는 dto
    private PlaceDto placeInfo;
    private List<String> top3photos;
    // 대표 썸네일
    private String thumbnail;
}
