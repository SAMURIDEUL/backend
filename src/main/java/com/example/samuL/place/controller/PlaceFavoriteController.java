package com.example.samuL.place.controller;


import com.example.samuL.common.okResponse.OkResponse;
import com.example.samuL.place.service.PlaceFavoriteService;
import com.example.samuL.user.service.CustomUserDetails;
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

    @PostMapping("/{placeId}/like")
    public ResponseEntity<OkResponse<Long>> addFavorite(
            @PathVariable Long placeId,
            Authentication authentication,
            HttpServletRequest request){
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();

        placeFavoriteService.addFavorite(userId, placeId);
        String path = request.getRequestURI();
        return ResponseEntity.ok(OkResponse.success("찜 추가 성공", placeId, path));
    }

    @DeleteMapping("{placeId}/like")
    public ResponseEntity<OkResponse<Long>> removeFavorite(
            @PathVariable Long placeId,
            Authentication authentication,
            HttpServletRequest request){
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();

        placeFavoriteService.removeFavorite(userId, placeId);
        String path = request.getRequestURI();

        return ResponseEntity.ok(OkResponse.success("찜 삭제 성공", placeId, path));
    }

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
