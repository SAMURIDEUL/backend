package com.example.samuL.review.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewWithPhotosDto {
    private Long id;
    private Long placeId;
    private Long userId;
    private Integer rating;
    private String content;
    private LocalDate visitDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ReviewPhotoDto> photos = new ArrayList<>();
}
