package com.example.samuL.place.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "반려동물 제한(제약) 사항에 관한 dto")
@Data
public class PetPolicyDto {
    // 제약 사항에 관한 dto

    @Schema(description = "반려동물 동반 가능 여부", example = "true")
    private Boolean petAllowed; // 반려동물 동반 가능
    @Schema(description = "반려동물 사이즈 제한", example = "모두 가능")
    private String petSizeLimit; // 반려동물 사이즈 제안
    @Schema(description = "실내 출입 가능 여부", example = "true")
    private Boolean indoorFlag;  // 실내 가능
    @Schema(description = "실외 출입 가능 여부", example = "false")
    private Boolean outdoorFlag; // 실외 가능
    @Schema(description = "그 외에 가게의 제한 사항", example = "제한사항 없음")
    private String petRestrictions; // 제한 사항
}
