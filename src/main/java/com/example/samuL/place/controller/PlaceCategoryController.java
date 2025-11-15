package com.example.samuL.place.controller;


import com.example.samuL.common.okResponse.OkResponse;
import com.example.samuL.place.dto.PlaceDto;
import com.example.samuL.place.dto.PlaceScrollResponse;
import com.example.samuL.place.service.PlaceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class PlaceCategoryController {
    private final PlaceService placeService;

    @GetMapping("/{categoryId}/places")
    public ResponseEntity<PlaceScrollResponse> getPlacesByCategory(
            @PathVariable Integer categoryId,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String subdistrict,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false)BigInteger lastId,
            @RequestParam(defaultValue = "20") int size
            ){

        if(categoryId > 13 || categoryId < 1){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다.");
        }

        if(size <= 0 || size > 100){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "size는 1 - 100 사이의 값이 어야 됩니다.");
        }

        PlaceScrollResponse response = placeService.getPlace(categoryId, city, district, subdistrict, keyword, lastId, size);
        return ResponseEntity.ok(response);

    }


//    @GetMapping("places/random")
//    public ResponseEntity<OkResponse<List<PlaceDto>>> getRandomPlaces(HttpServletRequest request){
//        List<PlaceDto> randomPlaces = placeService.getRandomPlaces();
//        return ResponseEntity.ok(OkResponse.success("6개 랜덤 추출 성공", randomPlaces, request.getRequestURI()) );
//    }


}
