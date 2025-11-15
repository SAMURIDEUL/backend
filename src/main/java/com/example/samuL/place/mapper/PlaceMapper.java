package com.example.samuL.place.mapper;


import com.example.samuL.place.dto.PlaceDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;

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
            @Param("lastId") BigInteger lastId,
            @Param("pageSize") int pageSize

    );

    @Select("SELECT COUNT(*) FROM places")
    long getTotalCount();

    PlaceDto getPlaceByOffset(@Param("offset") long offset);

}
