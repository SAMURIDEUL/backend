package com.example.samuL.place.service;

import com.example.samuL.place.dto.PlaceDetailDto;
import com.example.samuL.place.dto.PlaceDto;
import com.example.samuL.place.dto.PlaceReviewDto;
import com.example.samuL.place.dto.PlaceScrollResponse;

import com.example.samuL.place.mapper.PlaceMapper;


import com.example.samuL.place.mapper.PlaceReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService{
    private final PlaceMapper placeMapper;
    private final PlaceReviewMapper placeReviewMapper;

    @Override
    public PlaceScrollResponse getPlace(Integer categoryId,
                                        String city,
                                        String district,
                                        String subdistrict,
                                        String keyword,
                                        BigInteger lastId,
                                        int size){

        // 1. 장소 목록 조회
        List<PlaceDto> places = placeMapper.findPlaces(categoryId, city, district, subdistrict, keyword, lastId, size + 1);
        boolean hasNext = false;
        if(places.size() > size){
            hasNext = true;
            places = places.subList(0, size);
        }


        for (PlaceDto place : places){
            Long placeId = place.getId().longValue();
            Double avgScore = placeMapper.getAverageScoreByPlaceId(placeId);
            place.setAverageRating(avgScore);
        }

        // 다음 커서 계산
        Long nextCursor = hasNext ? places.get(places.size() - 1).getId().longValue() : null;

        return new PlaceScrollResponse(places, nextCursor, hasNext);
    }

    // 랜덤 6개
    private static Long cachedTotalCount = null;
    @Override
    public List<PlaceDto> getRandomPlaces(){
        if(cachedTotalCount == null){
            cachedTotalCount = placeMapper.getTotalCount(); // 최초 1회만 DB접근
        }

        long totalCount = cachedTotalCount;
        ThreadLocalRandom random = ThreadLocalRandom.current();

        Set<Long>offsets = new HashSet<>();
        while(offsets.size() < 6){
            offsets.add(random.nextLong(totalCount));
        }

        List<PlaceDto> places = new ArrayList<>();
        for(Long offset : offsets){
            PlaceDto place = placeMapper.getPlaceByOffset(offset);
            if(place != null){
                Long placeId = place.getId().longValue();
                Double avgScore = placeMapper.getAverageScoreByPlaceId(placeId);
                place.setAverageRating(avgScore);
                places.add(place);
            }

        }
        return places;
    }


}
