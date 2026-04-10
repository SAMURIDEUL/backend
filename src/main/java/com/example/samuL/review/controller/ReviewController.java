package com.example.samuL.review.controller;

import com.example.samuL.common.okResponse.OkResponse;
import com.example.samuL.review.dto.ReviewDto;
import com.example.samuL.review.dto.ReviewUpdateResponse;
import com.example.samuL.review.dto.ReviewWithPhotosDto;
import com.example.samuL.review.service.ReviewService;
import com.example.samuL.user.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/places/{placeId}/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "리뷰 저장", description = "주어진 리뷰와 사진들을 저장합니다.")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<OkResponse<ReviewDto>> createReview(
            @Parameter(description = "장소 id")
            @PathVariable Long placeId,
            @Parameter(description = "리뷰 내용")
            @RequestPart("review") ReviewDto reviewDto,
            @Parameter(description = "사진들")
            @RequestPart(value = "images", required = false)List<MultipartFile> images,
            Authentication authentication,
            HttpServletRequest request
            ) throws IOException{
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Long userId = user.getUserId();

        reviewDto.setPlaceId(placeId);

        ReviewDto saveReview = reviewService.addReview(reviewDto, images, userId);
        return ResponseEntity.ok(OkResponse.success(saveReview, request.getRequestURI()));
    }

    @Operation(summary = "리뷰 내용이나 사진 추가 및 삭제 (POST)", description = "리뷰 내용을 수정하거나 사진을 삭제하거나 추가할 수 있습니다. (Tomcat의 PUT 메서드 멀티파트 파싱 이슈로 인해 POST 사용)")
    @PostMapping(value = "/{reviewId}", consumes = "multipart/form-data")
    public ResponseEntity<ReviewUpdateResponse> updateReview(
            @PathVariable Long placeId,
            @Parameter(description = "리뷰 id", required = true)
            @PathVariable Long reviewId,
            @Parameter(description = "수정된 리뷰 내용")
            @RequestPart("review") ReviewWithPhotosDto reviewWithPhotosDto,
            @Parameter(description = "유지할 사진 리스트(배열) /즉, 사진 삭제 시에는 리스트(배열)에서 삭제할 사진 id를 지워주면 됩니다. ex([3, 5] => [3]으로 보내면 5번 사진이 삭제됨)")
            @RequestPart(value = "keepImageIds", required = false) List<Long> keepImageIds,
            @Parameter(description = "새롭게 추가할 사진 추가")
            @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
            Authentication authentication
    ) throws IOException{
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        ReviewUpdateResponse updated = reviewService.updateReview(reviewId, reviewWithPhotosDto, keepImageIds, newImages, userId);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<OkResponse<Void>> deleteReview(
            @Parameter(description = "리뷰 id")
            @PathVariable Long reviewId,
            Authentication authentication
            , HttpServletRequest request){
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();

        reviewService.deleteReview(reviewId, userId);

        return ResponseEntity.ok(OkResponse.success("리뷰 삭제 완료", request.getRequestURI()));
    }

}
