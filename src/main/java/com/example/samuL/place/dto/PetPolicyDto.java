package com.example.samuL.place.dto;

import lombok.Data;

@Data
public class PetPolicyDto {
    private Boolean petAllowed;
    private String petSizeLimit;
    private Boolean indoorFlag;
    private Boolean outdoorFlag;
    private String petRestrictions;
}
