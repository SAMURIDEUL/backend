package com.example.samuL.place.service;

import com.example.samuL.common.exception.custom.DuplicateFavoriteException;
import com.example.samuL.common.exception.custom.FavoriteNotFoundException;
import com.example.samuL.common.exception.custom.PlaceNotFoundException;
import com.example.samuL.common.exception.custom.UserNotFoundException;
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
        if(!placeFavoriteMapper.existsUserId(userId)){
            throw new UserNotFoundException("userid를 찾을 수 없습니다.");
        }
        if(!placeFavoriteMapper.existsPlaceId(placeId)){
            throw new PlaceNotFoundException("장소를 찾을 수 없습니다. placeId=" + placeId);
        }
        if(placeFavoriteMapper.existsFavorite(userId, placeId) > 0){
            throw new DuplicateFavoriteException("이미 찜한 장소입니다. placeId=" + placeId);
        }


        placeFavoriteMapper.insertFavorite(userId, placeId);
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long placeId){
        if(!placeFavoriteMapper.existsPlaceId(placeId)){
            throw new PlaceNotFoundException("장소를 찾을 수 없습니다. placeId=" + placeId);
        }
        int deleted = placeFavoriteMapper.deleteFavorite(userId, placeId);
        if(deleted == 0){
            throw new FavoriteNotFoundException("해당 장소는 찜 목록에 존재하지 않습니다.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getMyFavoritePlaceIds(Long userId){
        if (!placeFavoriteMapper.existsUserId(userId)){
            throw new UserNotFoundException("userid를 찾을 수 없습니다.");
        }

        return placeFavoriteMapper.selectFavoritePlaceIds(userId);
    }
}
