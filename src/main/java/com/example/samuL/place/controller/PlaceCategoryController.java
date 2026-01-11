package com.example.samuL.place.controller;


import com.example.samuL.common.okResponse.OkResponse;
import com.example.samuL.place.dto.PlaceSelectScroll;
import com.example.samuL.place.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    // 사진 추가, 썸네일 추가
    @Operation(summary = "카테고리 id의 장소들 조회", description = "각 카테로리 id(1 - 13)의 place들을 조회합니다.")
    @GetMapping("{categoryId}/places")
    public ResponseEntity<OkResponse<PlaceSelectScroll>> getPlacesCategory(
            @Parameter(description = "카테고리 id", required = true)
            @PathVariable Integer categoryId,
            @Parameter(description = "도/시")
            @RequestParam(required = false) String city,
            @Parameter(description = "구/군")
            @RequestParam(required = false) String district,
            @Parameter(description = "동/읍/면")
            @RequestParam(required = false) String subdistrict,
            @Parameter(description = "가게 이름으로 검색")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "다음 페이지 nextCursor값/ hasNext값이 true이면 다음 페이지가 존재함으로 lastId값으로 다음 페이지 이동/ 다음페이지 없으면 null 반환")
            @RequestParam(required = false)BigInteger lastId,
            @Parameter(description = "한 번에 가져올 장소 개수")
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request

    ){
        // 카테고리 유효성 검사
        if(categoryId > 13 || categoryId < 1){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다.");
        }
        // 페이지 유효성 검사
        if(size <= 0 || size > 100){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "size는 1 - 100 사이의 값이 어야 됩니다.");
        }

        PlaceSelectScroll response = placeService.getPlaceDetail(categoryId, city, district, subdistrict, keyword, lastId, size);

        return ResponseEntity.ok(OkResponse.success(response, request.getRequestURI()));

    }



}
