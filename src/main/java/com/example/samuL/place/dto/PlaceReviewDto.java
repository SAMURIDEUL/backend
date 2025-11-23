package com.example.samuL.place.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlaceReviewDto {
    private Long id;
    private Long placeId;
    private Long userId;
    private int rating;
    private String content;
    private LocalDateTime visitDate;
    private LocalDateTime createdAt;
    private List<String> photos;
}
