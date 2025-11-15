package com.example.samuL.review.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReviewDto {
    private Long id;
    private Long placeId;
    private Long userId;
    private int rating;
    private String content;
    private LocalDate visitDate;
}
