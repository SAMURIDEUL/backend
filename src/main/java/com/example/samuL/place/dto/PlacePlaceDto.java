package com.example.samuL.place.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "가게 조회, 가게+리뷰 조회용 dto")
@Data
public class PlacePlaceDto {
    // 가게 + 리뷰 조회용 가게 dto
    @Schema(description = "가게 id", example = "23849")
    private Long id;
    @Schema(description = "가게 이름", example = "YOLO오시개")
    private String name;
    @Schema(description = "가게 카테고리 이름", example = "카페")
    private String category3;

    @Schema(description = "시/도", example = "부산광역시")
    private String city;   // 시/도
    @Schema(description = "구/군", example = "사상구")
    private String district;  //구/군
    @Schema(description = "동/읍/면", example = "주례동")
    private String subdistrict;  //동/읍/면

    @Schema(description = "도로명 주소", example = "부산광역시 사상구 가야대로366번길 10")
    private String roadAddress;    // 도로명 주소
    @Schema(description = "우편번호", example = "47006")
    private String postalCode;     //우편번호
    @Schema(description = "전화번호", example = "0507-1318-4786")
    private String phone; // 전화번호

    @Schema(description = "위도", example = "35.150589")
    private Double lat;
    @Schema(description = "경도", example = "129.012454")
    private Double lon;

    @Schema(description = "업데이트 시간", example = "2025-10-19 16:23:24")
    private String updatedAt;
    @Schema(description = "카테고리 id", example = "3")
    private Long categoryId;

    @Schema(description = "주차 가능 여부", example = "true")
    private Boolean parkingAvailable;

    @Schema(description = "반려동물 제한 사항")
    private PetPolicyDto petPolicy;
    @Schema(description = "평균 별점/평점", example = "5.0")
    private Double averageRating;
}
