package com.example.samuL.review.mapper;

import com.example.samuL.review.dto.ReviewDto;
import com.example.samuL.review.dto.ReviewPhotoDto;
import com.example.samuL.review.dto.ReviewResponse;
import com.example.samuL.review.dto.ReviewWithPhotosDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {
    // 리뷰 추가
    void insertReview(ReviewDto reviewDto);

    // 리뷰 사진 추가
    void insertReviewPhoto(ReviewPhotoDto reviewPhotoDto);

    List<ReviewDto> findReviewsByPlaceId(Long placeId);

    // 리뷰 조회
    ReviewWithPhotosDto findById(@Param("reviewId") Long reviewId);

    // 리뷰 내용 수정
    void updateReview(ReviewWithPhotosDto reviewWithPhotosDto);

    // 리뷰 사진 조회
    List<ReviewPhotoDto> selectReviewPhotos(@Param("reviewId") Long reviewId);

    // 리뷰 사진 삭제
    void deletePhotoById(@Param("id") Long photoId);

    // 리뷰 사진 삭제
    Long findReviewOwner(Long reviewId);

    int deleteReview(Long reviewId);

    // 리뷰 조회
    List<ReviewResponse> getUserReviews(@Param("userId") Long userId,
            @Param("offset") int offset,
            @Param("size") int size);

    List<ReviewPhotoDto> getPhotosByReviewIds(@Param("list") List<Long> reviewIds);

    long countUserReviews(@Param("userId") Long userId);

}
