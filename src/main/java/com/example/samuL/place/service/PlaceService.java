package com.example.samuL.place.service;

import com.example.samuL.place.dto.PlaceDto;
import com.example.samuL.place.dto.PlaceScrollResponse;
import com.example.samuL.place.dto.PlaceSelectScroll;
import com.example.samuL.place.dto.RandomPlaceResponse;

import java.math.BigInteger;
import java.util.List;

public interface PlaceService {
    PlaceScrollResponse getPlace(Integer categoryId,
            String city,
            String district,
            String subdistrict,
            String keyword,
            BigInteger lastId,
            int size);

    PlaceSelectScroll getPlaceDetail(Integer categoryId,
            String city,
            String district,
            String subdistrict,
            String keyword,
            Double lat,
            Double lon,
            BigInteger lastId,
            int size);

    // place 랜덤 6개 조회
    public List<PlaceDto> getRandomPlaces();

    public List<RandomPlaceResponse> getRandomPlaceWithThumbnail();

}
