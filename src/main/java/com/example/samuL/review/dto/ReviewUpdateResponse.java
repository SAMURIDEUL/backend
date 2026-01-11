package com.example.samuL.review.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "리뷰 업데이트 반환용 dto")
@Data
public class ReviewUpdateResponse {
    @Schema(description = "업데이트된 리뷰 내용")
    private ReviewWithPhotosDto updatedReview;
    @Schema(description = "리뷰 업데이트 성공 여부")
    private boolean reviewUpdated;
    @Schema(description = "삭제된 사진 id", example = "[10]")
    private List<Long> deletePhotoIds;
    @Schema(description = "새롭게 추가된 사진", example = " {" +
            "            \"id\": null," +
            "            \"reviewId\": 7," +
            "            \"photoUrl\": \"/uploads/review_images/428bf1fa-ead6-4ec3-960c-122f2d89b335.jpg\"" +
            "        }")
    private List<ReviewPhotoDto> newPhotos;
}
