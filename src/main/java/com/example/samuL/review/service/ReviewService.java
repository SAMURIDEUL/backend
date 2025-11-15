package com.example.samuL.review.service;

import com.example.samuL.review.dto.ReviewDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReviewService {
    ReviewDto addReview(ReviewDto reviewDto, List<MultipartFile> imageFiles, Long userId) throws IOException;
    List<ReviewDto> getReviewsByPlace(Long placeId);
}
