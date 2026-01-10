package com.example.samuL.place.controller;

import com.example.samuL.common.okResponse.OkResponse;
import com.example.samuL.place.dto.PlaceLocDetailDto;
import com.example.samuL.place.service.PlaceLocService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceLocController {
    // 위치를 이용한 장소 검색 전용 controller
    private final PlaceLocService placeLocService;

    @GetMapping("/nearby")
    public ResponseEntity<OkResponse<List<PlaceLocDetailDto>>> getNearbyPlaces(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "2000") double radius,
            HttpServletRequest request
    ){
        List<PlaceLocDetailDto> nearbyPlaces = placeLocService.getNearbyPlaceWithPhoto(lat, lon, radius);
        return ResponseEntity.ok(OkResponse.success(nearbyPlaces,request.getRequestURI()));
    }

}
