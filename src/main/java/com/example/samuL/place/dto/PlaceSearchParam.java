package com.example.samuL.place.dto;

import lombok.Data;

@Data
public class PlaceSearchParam {
    private double lat;
    private double lon;
    private double radius; //meter, 반경
}
