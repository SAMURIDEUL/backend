package com.example.samuL.review.mapper;

import com.example.samuL.review.dto.ReviewDto;
import com.example.samuL.review.dto.ReviewPhotoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    void insertReview(ReviewDto reviewDto);
    void insertReviewPhoto(ReviewPhotoDto reviewPhotoDto);
    List<ReviewDto> findReviewsByPlaceId(Long placeId);
}
