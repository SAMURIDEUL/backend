package com.example.samuL.review.dto;

import lombok.Data;

@Data
public class ReviewPhotoDto {
    private Long id;
    private Long reviewId;
    private String photoUrl;
}
