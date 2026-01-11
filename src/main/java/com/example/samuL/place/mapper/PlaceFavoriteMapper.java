package com.example.samuL.place.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlaceFavoriteMapper {

    int insertFavorite(@Param("userId") Long userId,
                       @Param("placeId") Long placeId);

    int deleteFavorite(@Param("userId") Long userId,
                       @Param("placeId") Long placeId);

    int existsFavorite(@Param("userId") Long userId,
                       @Param("placeId")Long placeId);

    List<Long> selectFavoritePlaceIds(@Param("userId") Long userId);

    boolean existsUserId(Long userId);
    boolean existsPlaceId(Long placeId);
    boolean existFavorite(Long userId, Long placeId);
}
