package com.example.samuL.place.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PlaceSelectScroll {
    private List<PlaceSelectDetailDto> places;
    private Long nextCursor;
    private boolean hasNext;
}
