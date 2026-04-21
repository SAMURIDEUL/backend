package com.example.samuL.place.mapper;

import com.example.samuL.place.dto.PlaceDto;
import com.example.samuL.place.dto.PlacePlaceDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

// keyset pagination(cusor-based pagination)
// 복합 인덱스 추가 필요
@Mapper
public interface PlaceMapper {
    List<PlaceDto> findPlaces(
            @Param("categoryId") Integer categoryId,
            @Param("city") String city,
            @Param("district") String district,
            @Param("subdistrict") String subdistrict,
            @Param("keyword") String keyword,
            @Param("lat") Double lat,
            @Param("lon") Double lon,
            @Param("lastId") BigInteger lastId,
            @Param("pageSize") int pageSize

    );

    default Double getAverageScoreId(Long placeId) {
        List<Map<String, Object>> list = getAverageScores(List.of(placeId));
        if (list != null && !list.isEmpty()) {
            return ((Number) list.get(0).get("avgScore")).doubleValue();
        }
        return 0.0;
    }

    List<Map<String, Object>> getAverageScores(@Param("placeIds") List<Long> placeIds);

    List<String> getPhotoUrlsByPlaceId(@Param("placeId") Long placeId);

    List<Map<String, Object>> getPhotoUrls(@Param("placeIds") List<Long> placeIds);

    @Select("SELECT COUNT(*) FROM places")
    long getTotalCount();

    PlaceDto getPlaceByOffset(@Param("offset") long offset);

    //
    PlacePlaceDto getPlaceOffset(@Param("offset") long offset);

    Double getAverageScoreByPlaceId(Long placeId);
}
