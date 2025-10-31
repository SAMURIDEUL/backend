package com.example.samuL.place.service;

import com.example.samuL.place.dto.PlaceDto;
import com.example.samuL.place.dto.PlaceScrollResponse;

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

    public List<PlaceDto> getRandomPlaces();
}
