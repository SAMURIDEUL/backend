package com.example.samuL.place.service;


import com.example.samuL.common.exception.custom.InvalidLocationException;
import com.example.samuL.place.dto.PlaceLocDetailDto;
import com.example.samuL.place.dto.PlacePlaceDto;
import com.example.samuL.place.image.CategoryDefaultImage;
import com.example.samuL.place.mapper.PlaceLocMapper;
import com.example.samuL.place.mapper.PlaceMapper;
import com.example.samuL.place.mapper.PlaceReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceLocServiceImpl implements PlaceLocService{
    private final PlaceLocMapper placeLocMapper;
    private final PlaceReviewMapper placeReviewMapper;
    private final PlaceMapper placeMapper;

    @Override
    public List<PlaceLocDetailDto> getNearbyPlaceWithPhoto(double lat, double lon, double radius){

        if (lat < -90 || lat > 90){
            throw new InvalidLocationException("위도(lat)은 -90에서 90 사이여야 합니다.");
        }

        if(lon < -180 || lon > 180){
            throw new InvalidLocationException("경도(lon)은 -180에서 180 사이여야 합니다.");
        }

        if(radius <= 0){
            throw new InvalidLocationException("반경(radius)는 0보다 커야 합니다.");
        }


        List<PlacePlaceDto> places = placeLocMapper.selectNearbyPlaces(lat, lon);

        List<PlaceLocDetailDto> result = new ArrayList<>();

        for (PlacePlaceDto place : places) {
            double distance = distanceInMeters(lat, lon, place.getLat(), place.getLon());
            if(distance > radius) continue;

            Long placeId = place.getId();

            // 평점
            Double avgScore = placeMapper.getAverageScoreByPlaceId(placeId);
            place.setAverageRating(avgScore);

            // 사진 한장 가져오기
            List<String> allPhotos = placeReviewMapper.getPhotoUrlsByPlaceId(placeId);
            String photo;
            if (allPhotos != null && !allPhotos.isEmpty()){
                Collections.shuffle(allPhotos);
                photo = allPhotos.get(0);
            } else {
                photo = CategoryDefaultImage.getDefaultImage(place.getCategoryId());
            }

            PlaceLocDetailDto dto = new PlaceLocDetailDto();
            dto.setPlaceInfo(place);
            dto.setPhoto(photo);

            result.add(dto);
        }

        return result;
    }

    // 거리 계산 함수 (Haversine 공식)
    private double distanceInMeters(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // 지구 반지름 (m)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) *
                        Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }



}
