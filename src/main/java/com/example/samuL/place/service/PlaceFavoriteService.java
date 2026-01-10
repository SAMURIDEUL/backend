package com.example.samuL.place.service;

import java.util.List;

public interface PlaceFavoriteService {
    void addFavorite(Long userId, Long placeId);
    void removeFavorite(Long userId, Long placeId);
    List<Long> getMyFavoritePlaceIds(Long userId);
}
