package com.example.samuL.review.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewPhotoDto {
    private Long id;
    private Long reviewId;
    private String PhotoUrl;
    private LocalDateTime createdAt;
}
