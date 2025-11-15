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

    // place 랜덤 6개 조회
    public List<PlaceDto> getRandomPlaces();

    //리뷰와 사진과 함께 조회
   // public PlaceDetailDto getPlaceDetail(Long placeId);

}
