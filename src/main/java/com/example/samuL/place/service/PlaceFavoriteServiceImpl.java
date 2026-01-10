package com.example.samuL.place.service;

import com.example.samuL.place.mapper.PlaceFavoriteMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PlaceFavoriteServiceImpl implements PlaceFavoriteService{
    private final PlaceFavoriteMapper placeFavoriteMapper;

    @Override
    @Transactional
    public void addFavorite(Long userId, Long placeId){
        if(placeFavoriteMapper.existsFavorite(userId, placeId) > 0){
            throw new IllegalStateException("이미 찜한 장소입니다.");
        }
        placeFavoriteMapper.insertFavorite(userId, placeId);
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long placeId){
        placeFavoriteMapper.deleteFavorite(userId, placeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getMyFavoritePlaceIds(Long userId){
        return placeFavoriteMapper.selectFavoritePlaceIds(userId);
    }
}
