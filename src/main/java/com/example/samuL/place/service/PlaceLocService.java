package com.example.samuL.place.service;

import com.example.samuL.place.dto.PlaceLocDetailDto;

import java.util.List;

public interface PlaceLocService {

    List<PlaceLocDetailDto> getNearbyPlaceWithPhoto(double lat, double lon, double radius);

}
