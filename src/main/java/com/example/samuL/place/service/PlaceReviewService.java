package com.example.samuL.place.service;

import com.example.samuL.place.dto.PlaceDetailDto;
import com.example.samuL.place.dto.ReviewPageDto;

public interface PlaceReviewService {
    PlaceDetailDto getPlaceDetail(Long placeId);
    ReviewPageDto getReviewsPaged(Long placeId, int page, int size);
}
