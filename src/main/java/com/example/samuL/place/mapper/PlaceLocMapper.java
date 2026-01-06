package com.example.samuL.place.mapper;

import com.example.samuL.place.dto.PlaceDetailDto;
import com.example.samuL.place.dto.PlaceDto;
import com.example.samuL.place.dto.PlacePlaceDto;
import com.example.samuL.place.dto.PlaceSearchParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface PlaceLocMapper {

    // 근처 30개 장소 가져오기
    List<PlacePlaceDto> selectNearbyPlaces(
            @Param("lat") double lat,
            @Param("lon") double lon
    );



    // 평균 별점 조회
    Double getAverageScore(Long placeId);
}
