package com.example.samuL.place.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class PlaceDto {
    private BigInteger id;
    private String name;
    private String city;
    private String district;
    private String subdistrict;
    private String road_address;
    private String postal_code;
    private String phone;
    private Short category_id;
}
