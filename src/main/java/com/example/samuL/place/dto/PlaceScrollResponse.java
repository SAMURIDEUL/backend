package com.example.samuL.place.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PlaceScrollResponse {
    private List<PlaceDto> data;
    private Long nextCursor;
    private boolean hasNext;
}
