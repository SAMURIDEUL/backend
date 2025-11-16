package com.example.samuL.review.dto;


import lombok.Data;

import java.util.List;

@Data
public class ReviewUpdateResponse {
    private ReviewWithPhotosDto updatedReview;
    private boolean reviewUpdated;
    private List<Long> deletePhotoIds;
    private List<ReviewPhotoDto> newPhotos;
}
