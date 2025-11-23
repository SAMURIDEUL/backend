package com.example.samuL.place.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlaceDetailDto {
    private PlacePlaceDto placeInfo;
    private List<String> top3photos;

}
