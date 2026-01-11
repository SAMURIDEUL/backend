package com.example.samuL.place.controller;


import com.example.samuL.common.okResponse.OkResponse;
import com.example.samuL.place.service.PlaceFavoriteService;
import com.example.samuL.user.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceFavoriteController {
    private final PlaceFavoriteService placeFavoriteService;

    @Operation(summary = "찜 추가", description = "주어진 장소 id를 찜 목록에 추가합니다.")
    @PostMapping("/{placeId}/like")
    public ResponseEntity<OkResponse<Long>> addFavorite(
            @Parameter(description = "placeId", required = true)
            @PathVariable Long placeId,
            Authentication authentication,
            HttpServletRequest request){
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();

        placeFavoriteService.addFavorite(userId, placeId);
        String path = request.getRequestURI();
        return ResponseEntity.ok(OkResponse.success("찜 추가 성공", placeId, path));
    }

    @Operation(summary = "찜 삭제", description = "찜 목록에서 주어진 장소 id를 삭제합니다.")
    @DeleteMapping("{placeId}/like")
    public ResponseEntity<OkResponse<Long>> removeFavorite(
            @Parameter(description = "placeId", required = true)
            @PathVariable Long placeId,
            Authentication authentication,
            HttpServletRequest request){
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();

        placeFavoriteService.removeFavorite(userId, placeId);
        String path = request.getRequestURI();

        return ResponseEntity.ok(OkResponse.success("찜 삭제 성공", placeId, path));
    }

    @Operation(summary = "찜 목록 조회", description = "찜 목록을 조회합니다.")
    @GetMapping("/likes")
    public ResponseEntity<OkResponse<List<Long>>> getMyFavoritePlaces(
            Authentication authentication,
            HttpServletRequest request
    ){
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        List<Long> favoritePlaceIds = placeFavoriteService.getMyFavoritePlaceIds(userId);
        String path = request.getRequestURI();
        return ResponseEntity.ok(OkResponse.success(favoritePlaceIds, path));
    }
}
