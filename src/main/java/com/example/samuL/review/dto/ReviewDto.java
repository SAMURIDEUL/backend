package com.example.samuL.review.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReviewDto {
    private int id;
    private int placeId;
    private int userId;
    private byte rating;
    private String content;
    private LocalDate visitDate;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
