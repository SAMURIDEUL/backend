package com.example.samuL.review.service;

import com.example.samuL.review.dto.ReviewDto;
import com.example.samuL.review.dto.ReviewUpdateResponse;
import com.example.samuL.review.dto.ReviewWithPhotosDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReviewService {
    //리뷰 작성
    ReviewDto addReview(ReviewDto reviewDto, List<MultipartFile> imageFiles, Long userId) throws IOException;
    List<ReviewDto> getReviewsByPlace(Long placeId);
    //리뷰 수정
    ReviewUpdateResponse updateReview(Long reviewId,
                                      ReviewWithPhotosDto reviewDto,
                                      List<Long> keepImageIds,
                                      List<MultipartFile> newImages,
                                      Long userId) throws IOException;
}
