package com.example.samuL.place.dto;

import lombok.Data;

@Data
public class PlacePlaceDto {
    // 가게 + 리뷰 조회용 가게 dto
    private Long id;
    private String name;
    private String category3;

    private String city;   // 시/도
    private String district;  //구/군
    private String subdistrict;  //동/읍/면

    private String roadAddress;    // 도로명 주소
    private String postalCode;     //우편번호
    private String phone; // 전화번호

    private Double lat;
    private Double lon;

    private String updatedAt;
    private Long categoryId;
}
