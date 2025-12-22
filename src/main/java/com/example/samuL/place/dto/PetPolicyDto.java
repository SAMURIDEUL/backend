package com.example.samuL.place.dto;

import lombok.Data;

@Data
public class PetPolicyDto {
    private Long placeId;
    private Boolean petAllowed; // 반려동물 동반 가능
    private String petSizeLimit; // 반려동물 사이즈 제안
    private Boolean indoorFlag;  // 실내 가능
    private Boolean outdoorFlag; // 실외 가능
    private String petRestrictions; // 제한 사항
}
