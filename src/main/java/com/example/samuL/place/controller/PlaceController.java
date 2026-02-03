package com.example.samuL.place.controller;

import com.example.samuL.common.okResponse.OkResponse;
import com.example.samuL.place.dto.PlaceDetailDto;
//import com.example.samuL.place.dto.PlaceDto;
import com.example.samuL.place.dto.RandomPlaceResponse;
import com.example.samuL.place.dto.ReviewPageDto;
import com.example.samuL.place.service.PlaceReviewService;
import com.example.samuL.place.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;
    private final PlaceReviewService placeReviewService;

    @Operation(summary = "랜덤 6개 장소 조회", description = "랜덤으로 정해진 6개의 장소를 반환합니다.")
    @GetMapping("/random")
    public ResponseEntity<OkResponse<List<RandomPlaceResponse>>> getRandom(HttpServletRequest request) {
        List<RandomPlaceResponse> response = placeService.getRandomPlaceWithThumbnail();

        return ResponseEntity.ok(OkResponse.success("6개 랜덤 추출 성공", response, request.getRequestURI()));
    }

    @Operation(summary = "placeId에 해당하는 장소 조회", description = "주어진 placeId에 대한 장소 정보를 반환합니다.")
    @GetMapping("/{placeId}")
    public ResponseEntity<OkResponse<PlaceDetailDto>> getPlaceDetail(
            @Parameter(description = "placeId", required = true) @PathVariable Long placeId,
            HttpServletRequest request) {
        PlaceDetailDto placeDetail = placeReviewService.getPlaceDetail(placeId);
        return ResponseEntity.ok(OkResponse.success(placeDetail, request.getRequestURI()));
    }

    @Operation(summary = "placeId에 해당하는 장소의 리뷰들 조회", description = "주어진 placeId에 대한 장소에 적힌 리뷰들을 반환합니다. 최신 리뷰 순으로 조회합니다.")
    @GetMapping("/{placeId}/reviews")
    public ResponseEntity<OkResponse<ReviewPageDto>> getReviewsPaged(
            @Parameter(description = "placeId", required = true) @PathVariable Long placeId,
            @Parameter(description = "현재 페이지, 만약 hasNext가 true라면 다음 페이지가 있다는 뜻으로 현재 페이지 + 1을 하면 다음 페이지") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "한 번에 가져올 리뷰들의 개수") @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        ReviewPageDto reviews = placeReviewService.getReviewsPaged(placeId, page, size);
        return ResponseEntity.ok(OkResponse.success(reviews, request.getRequestURI()));
    }

}
