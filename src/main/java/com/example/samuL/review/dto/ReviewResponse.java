package com.example.samuL.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private Long id;
    private Long placeId;
    private int rating;
    private String content;
    private LocalDate visitDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<String> photoUrls;
}
