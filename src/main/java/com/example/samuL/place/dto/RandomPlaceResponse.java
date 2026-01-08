package com.example.samuL.place.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RandomPlaceResponse {
    private PlacePlaceDto places;
    private String thumbnail;
}
