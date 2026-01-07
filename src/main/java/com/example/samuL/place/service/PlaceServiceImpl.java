package com.example.samuL.place.service;

import com.example.samuL.place.dto.*;

import com.example.samuL.place.image.CategoryDefaultImage;
import com.example.samuL.place.mapper.PlaceMapper;


import com.example.samuL.place.mapper.PlaceReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService{
    private final PlaceMapper placeMapper;
    private final PlaceReviewMapper placeReviewMapper;

    @Override
    public PlaceScrollResponse getPlace(Integer categoryId,
                                        String city,
                                        String district,
                                        String subdistrict,
                                        String keyword,
                                        BigInteger lastId,
                                        int size){

        // 1. 장소 목록 조회
        List<PlaceDto> places = placeMapper.findPlaces(categoryId, city, district, subdistrict, keyword, lastId, size + 1);
        boolean hasNext = false;
        if(places.size() > size){
            hasNext = true;
            places = places.subList(0, size);
        }


        for (PlaceDto place : places){
            Long placeId = place.getId().longValue();
            Double avgScore = placeMapper.getAverageScoreByPlaceId(placeId);
            place.setAverageRating(avgScore);
        }

        // 다음 커서 계산
        Long nextCursor = hasNext ? places.get(places.size() - 1).getId().longValue() : null;

        return new PlaceScrollResponse(places, nextCursor, hasNext);
    }

    @Override
    public PlaceSelectScroll getPlaceDetail(Integer categoryId,
                                     String city,
                                     String district,
                                     String subdistrict,
                                     String keyword,
                                     BigInteger lastId,
                                     int size){
        // 1. 장소 목록 조회
        List<PlaceDto> places = placeMapper.findPlaces(categoryId, city, district, subdistrict, keyword, lastId, size + 1);
        boolean hasNext = false;
        if(places.size() > size){
            hasNext = true;
            places = places.subList(0, size);
        }

        if (places.isEmpty()) {
            return new PlaceSelectScroll(List.of(), null, false);
        }

        // placeId 리스트 추출
        List<Long> placeIds = places.stream().map(p->p.getId().longValue()).toList();

        // 평점 조회
        Map<Long, Double> avgScoreMap = placeMapper.getAverageScores(placeIds);

        // 사진 한 방 조회
        List<Map<String, Object>> photoRows = placeMapper.getPhotoUrls(placeIds);

        Map<Long, List<String>> photoMap = new HashMap<>();
        for(Map<String, Object> row : photoRows){
            Long placeId = ((Number) row.get("placeId")).longValue();
            String photoUrl = (String)row.get("photoUrl");

            photoMap.computeIfAbsent(placeId, k->new ArrayList<>()).add(photoUrl);
        }

        //placeSelectDetaildto 만들기
        List<PlaceSelectDetailDto> detailList = new ArrayList<>();

        for (PlaceDto place:places){
            Long placeId = place.getId().longValue();
            // 평점 세팅
            place.setAverageRating(avgScoreMap.getOrDefault(placeId, 0.0));
            // 사진 3개
            List<String> allPhotos = photoMap.get(placeId);
            String defaultImg = CategoryDefaultImage.getDefaultImage(place.getCategoryId().longValue());

            List<String> top3Photos = makeTop3Photos(allPhotos, defaultImg);

            String thumbnail = top3Photos.get(0);

            PlaceSelectDetailDto dto = new PlaceSelectDetailDto();
            dto.setPlaceInfo(place);
            dto.setTop3photos(top3Photos);
            dto.setThumbnail(thumbnail);

            detailList.add(dto);
        }

        // 다음 커서
        Long nextCursor = hasNext?places.get(places.size() - 1).getId().longValue():null;

        return new PlaceSelectScroll(detailList, nextCursor, hasNext);
    }

    private List<String> makeTop3Photos(List<String> allPhotos, String defaultImg) {

        List<String> top3 = new ArrayList<>();

        if (allPhotos != null && !allPhotos.isEmpty()) {
            Collections.shuffle(allPhotos);
            top3.addAll(allPhotos.stream().limit(3).toList());
        }

        while (top3.size() < 3) {
            top3.add(defaultImg);
        }

        return top3;
    }



    // 랜덤 6개
    private static Long cachedTotalCount = null;
    @Override
    public List<PlaceDto> getRandomPlaces(){
        if(cachedTotalCount == null){
            cachedTotalCount = placeMapper.getTotalCount(); // 최초 1회만 DB접근
        }

        long totalCount = cachedTotalCount;
        ThreadLocalRandom random = ThreadLocalRandom.current();

        Set<Long>offsets = new HashSet<>();
        while(offsets.size() < 6){
            offsets.add(random.nextLong(totalCount));
        }

        List<PlaceDto> places = new ArrayList<>();
        for(Long offset : offsets){
            PlaceDto place = placeMapper.getPlaceByOffset(offset);
            if(place != null){
                Long placeId = place.getId().longValue();
                Double avgScore = placeMapper.getAverageScoreByPlaceId(placeId);
                place.setAverageRating(avgScore);
                places.add(place);
            }

        }
        return places;
    }


}
