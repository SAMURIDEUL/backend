package com.example.samuL.place.service;


import com.example.samuL.place.dto.PlaceDetailDto;
import com.example.samuL.place.dto.PlacePlaceDto;
import com.example.samuL.place.dto.PlaceReviewDto;
import com.example.samuL.place.dto.ReviewPageDto;
import com.example.samuL.place.image.CategoryDefaultImage;
import com.example.samuL.place.mapper.PlaceReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceReviewServiceImpl implements PlaceReviewService {
    private final PlaceReviewMapper placeReviewMapper;
    public PlaceDetailDto getPlaceDetail(Long placeId){
        PlacePlaceDto placeInfo = placeReviewMapper.getPlaceById(placeId);
        String defaultImg = CategoryDefaultImage.getDefaultImage(placeInfo.getCategoryId());

        List<String> allphotos = placeReviewMapper.getPhotoUrlsByPlaceId(placeId);
        List<String> top3Photos = new ArrayList<>();

        if(allphotos != null && !allphotos.isEmpty()){
            Collections.shuffle(allphotos);
            top3Photos.addAll(allphotos.stream().limit(3).toList());
        }

        while (top3Photos.size() < 3){
            top3Photos.add(defaultImg);
        }

        PlaceDetailDto dto = new PlaceDetailDto();
        dto.setPlaceInfo(placeInfo);
        dto.setTop3photos(top3Photos);

        return dto;
    }

    public ReviewPageDto getReviewsPaged(Long placeId, int page, int size){
        int offset = page * size;

        List<PlaceReviewDto> reviews = placeReviewMapper.getReviewsByPlacePaged(placeId, offset, size);

        for(PlaceReviewDto review : reviews){
            review.setPhotos(placeReviewMapper.getReviewPhotos(review.getId()));
        }

        int totalCount = placeReviewMapper.countReviewsByPlace(placeId);
        boolean hasNext = totalCount > (offset + size);

        ReviewPageDto dto = new ReviewPageDto();
        dto.setReviews(reviews);
        dto.setHasNext(hasNext);

        return dto;
    }
}
