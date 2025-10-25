package com.example.samuL.place.controller;


import com.example.samuL.place.dto.PlaceScrollResponse;
import com.example.samuL.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("/{categoryId}/places")
    public ResponseEntity<PlaceScrollResponse> getPlacesByCategory(
            @PathVariable Integer categoryId,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String subdistrict,
            @RequestParam(required = false)BigInteger lastId,
            @RequestParam(defaultValue = "20") int size
            ){

        PlaceScrollResponse response = placeService.getPlace(categoryId, city, district, subdistrict,lastId, size);
        return ResponseEntity.ok(response);

    }


}
