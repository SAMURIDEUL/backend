package com.example.samuL.review.controller;

import com.example.samuL.common.okResponse.OkResponse;
import com.example.samuL.review.dto.ReviewDto;
import com.example.samuL.review.dto.ReviewUpdateResponse;
import com.example.samuL.review.dto.ReviewWithPhotosDto;
import com.example.samuL.review.service.ReviewService;
import com.example.samuL.user.service.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/place/{placeId}/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ReviewDto> createReview(
            @PathVariable Long placeId,
            @RequestPart("review") ReviewDto reviewDto,
            @RequestPart(value = "images", required = false)List<MultipartFile> images,
            Authentication authentication
            ) throws IOException{
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Long userId = user.getUserId();

        reviewDto.setPlaceId(placeId);

        ReviewDto saveReview = reviewService.addReview(reviewDto, images, userId);
        return ResponseEntity.ok(saveReview);
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviewsByPlace(
            @PathVariable Long placeId
    ) {
        List<ReviewDto> reviews = reviewService.getReviewsByPlace(placeId);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping(value = "/{reviewId}", consumes = "multipart/form-data")
    public ResponseEntity<ReviewUpdateResponse> updateReview(
            @PathVariable Long placeId,
            @PathVariable Long reviewId,
            @RequestPart("review") ReviewWithPhotosDto reviewWithPhotosDto,
            @RequestPart(value = "keepImageIds", required = false) List<Long> keepImageIds,
            @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
            Authentication authentication
    ) throws IOException{
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        ReviewUpdateResponse updated = reviewService.updateReview(reviewId, reviewWithPhotosDto, keepImageIds, newImages, userId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<OkResponse<Void>> deleteReview(@PathVariable Long reviewId, Authentication authentication
            , HttpServletRequest request){
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();

        reviewService.deleteReview(reviewId, userId);

        return ResponseEntity.ok(OkResponse.success("리뷰 삭제 완료", request.getRequestURI()));
    }

}
