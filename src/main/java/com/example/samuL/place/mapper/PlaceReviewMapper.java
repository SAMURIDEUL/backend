package com.example.samuL.place.mapper;

import com.example.samuL.place.dto.PlacePlaceDto;
import com.example.samuL.place.dto.PlaceReviewDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlaceReviewMapper {
    PlacePlaceDto getPlaceById(@Param("placeId") Long placeId);

    List<PlaceReviewDto> getReviewsByPlacePaged(
            @Param("placeId") Long placeId,
            @Param("offset") int offset,
            @Param("limit") int limit);

    List<String> getReviewPhotos(@Param("reviewId") Long reviewId);

    List<String> getPhotoUrlsByPlaceId(@Param("placeId") Long placeId);

    int countReviewsByPlace(@Param("placeId") Long placeId);
}
