package com.example.samuL.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewPaginatedResponse<T>{
    private List<T> content; // 실제 데이터
    private int page; // 현재 페이지
    private int size; // 페이지 크기
    private long totalElements; // 전체 수
    private int totalPages; // 전체 페이지 수
}
