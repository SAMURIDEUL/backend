package com.example.samuL.place.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class PlaceDto {
    private BigInteger id;
//    private Long id;
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
    private Integer categoryId; // 카테고리 아이디

    private Boolean parkingAvailable; // 주차 가능 여부
    private PetPolicyDto petPolicy;
    private Double averageRating; // 평균 별점
//    private Integer reviewCount;
}
