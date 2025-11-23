package com.example.samuL.place.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReviewPageDto {
    private List<PlaceReviewDto> reviews;
    private boolean hasNext;
}
