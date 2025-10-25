package com.example.samuL.place.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class PlaceDto {
    private BigInteger id;
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
}

//CREATE TABLE places (
//        id BIGINT AUTO_INCREMENT PRIMARY KEY,        -- 장소 고유 ID
//                name VARCHAR(150) NOT NULL,                  -- 장소 이름
//category3 VARCHAR(80),                       -- 최종 카테고리
//
//  -- 지역/주소 정보
//city VARCHAR(60),                            -- 시/도
//district VARCHAR(60),                        -- 구/군
//subdistrict VARCHAR(60),                     -- 동/읍/면
//road_address VARCHAR(255),                   -- 도로명 주소
//postal_code VARCHAR(10),                     -- 우편번호
//
//  -- 연락 및 기타 정보
//phone VARCHAR(40),
//website VARCHAR(255),
//description TEXT,
//
//  -- 위치 정보
//lat DECIMAL(9,6),
//lon DECIMAL(9,6),
//geom POINT GENERATED ALWAYS AS (POINT(lon, lat)) STORED NOT NULL,
//
//        -- 데이터 업데이트 시각
//updated_at TIMESTAMP NOT NULL
//DEFAULT CURRENT_TIMESTAMP
//ON UPDATE CURRENT_TIMESTAMP,
//
//        -- 중복 방지 및 검색용 인덱스
//UNIQUE KEY uniq_place (name, road_address, district, city),
//KEY idx_region (city, district),
//SPATIAL INDEX spx_geom (geom)
//);