package com.example.samuL.place.controller;


import com.example.samuL.common.okResponse.OkResponse;
import com.example.samuL.place.dto.PlaceDetailDto;
import com.example.samuL.place.dto.PlaceDto;
import com.example.samuL.place.dto.ReviewPageDto;
import com.example.samuL.place.service.PlaceReviewService;
import com.example.samuL.place.service.PlaceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;
    private final PlaceReviewService placeReviewService;

    @GetMapping("/random")
    public ResponseEntity<OkResponse<List<PlaceDto>>> getRandomPlaces(HttpServletRequest request){
        List<PlaceDto> randomPlaces = placeService.getRandomPlaces();
        return ResponseEntity.ok(OkResponse.success("6개 랜덤 추출 성공", randomPlaces, request.getRequestURI()) );
    }

    @GetMapping("/{placeId}")
    public ResponseEntity<PlaceDetailDto> getPlaceDetail(@PathVariable Long placeId){
        return ResponseEntity.ok(placeReviewService.getPlaceDetail(placeId));
    }

    @GetMapping("/{placeId}/reviews")
    public ResponseEntity<ReviewPageDto> getReviewsPaged(
            @PathVariable Long placeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(placeReviewService.getReviewsPaged(placeId, page, size));
    }

//    @GetMapping("{placeId}")
//    public ResponseEntity<PlaceDetailDto> getPlaceDetail(@PathVariable Long placeId){
//        PlaceDetailDto placeDetail = placeService.getPlaceDetail(placeId);
//        return ResponseEntity.ok(placeDetail);
//    }


}
