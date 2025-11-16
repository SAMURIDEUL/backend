package com.example.samuL.review.service;

import com.example.samuL.common.properties.FileStorageProperties;
import com.example.samuL.review.dto.ReviewDto;
import com.example.samuL.review.dto.ReviewPhotoDto;
import com.example.samuL.review.dto.ReviewUpdateResponse;
import com.example.samuL.review.dto.ReviewWithPhotosDto;
import com.example.samuL.review.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewMapper reviewMapper;
    private final FileStorageProperties fileStorageProperties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReviewDto addReview(ReviewDto reviewDto,
                               List<MultipartFile> imageFiles,
                               Long userId) throws IOException{
        reviewDto.setUserId(userId);
        reviewMapper.insertReview(reviewDto);

        Path uploadDir = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        Files.createDirectories(uploadDir);

        if(imageFiles != null && !imageFiles.isEmpty()){
            for(MultipartFile file : imageFiles){
                if(file.isEmpty()) continue;

                String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
                String filename = UUID.randomUUID() + (ext != null ? "." + ext : "");

                Path filePath = uploadDir.resolve(filename);
                file.transferTo(filePath.toFile());

                //System.out.println("파일 저장 경로: " + filePath.toAbsolutePath());
                String prefix = fileStorageProperties.getAccessUrlPrefix();
                if (!prefix.endsWith("/")) prefix += "/";
                ReviewPhotoDto photo = new ReviewPhotoDto();
                photo.setReviewId(reviewDto.getId());
                photo.setPhotoUrl(prefix + filename);
                reviewMapper.insertReviewPhoto(photo);
            }
        }
        return reviewDto;
    }

    @Override
    public List<ReviewDto> getReviewsByPlace(Long placeId){
        return reviewMapper.findReviewsByPlaceId(placeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReviewUpdateResponse updateReview(Long reviewId,
                                  ReviewWithPhotosDto reviewWithPhotosDto,
                                  List<Long> keepImageIds,
                                  List<MultipartFile> newImages,
                                  Long userId
                                  ) throws IOException{

        Path uploadDir = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        ReviewWithPhotosDto oldReview = reviewMapper.findById(reviewId);
        // 리뷰 내용 수정
        oldReview.setPhotos(reviewMapper.selectReviewPhotos(reviewId));
        reviewWithPhotosDto.setId(reviewId);

        boolean reviewUpdated = !oldReview.equals(reviewWithPhotosDto);
        if(reviewUpdated){
            reviewMapper.updateReview(reviewWithPhotosDto);
        }
       // reviewMapper.updateReview(reviewWithPhotosDto);

        // 기존에 있던 이미지 조회
        List<Long> deletedPhotoIds = new ArrayList<>();
        List<ReviewPhotoDto> oldPhotos = reviewMapper.selectReviewPhotos(reviewId);

        // 삭제할 이미지 제거
        if (keepImageIds != null) {
            for (ReviewPhotoDto photo : oldPhotos) {
                if (!keepImageIds.contains(photo.getId())) {
                    if (photo.getPhotoUrl() != null && !photo.getPhotoUrl().isEmpty()) {
                        String filename = photo.getPhotoUrl().substring(photo.getPhotoUrl().lastIndexOf("/") + 1);
                        Files.deleteIfExists(uploadDir.resolve(filename));
                        reviewMapper.deletePhotoById(photo.getId());
                        deletedPhotoIds.add(photo.getId()); // response용
                    }
                    //reviewMapper.deletePhotoById(photo.getId());
                }
            }
        }

        List<ReviewPhotoDto> addedPhotos = new ArrayList<>();

        if(newImages != null){
            for(MultipartFile file: newImages){
                if(file.isEmpty()) continue;

                String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
                String filename = UUID.randomUUID() + (ext != null ? "." + ext : "");

                Path filePath = uploadDir.resolve(filename);
                file.transferTo(filePath.toFile());


                ReviewPhotoDto newPhoto = new ReviewPhotoDto();
                newPhoto.setReviewId(reviewId);
                String prefix = fileStorageProperties.getAccessUrlPrefix();
                newPhoto.setPhotoUrl(prefix + filename);

                reviewMapper.insertReviewPhoto(newPhoto);
                addedPhotos.add(newPhoto); // response용
            }
        }

        // 최종 리뷰 + 사진 조회 후 반환
        ReviewWithPhotosDto updated = reviewMapper.findById(reviewId);
        updated.setPhotos(reviewMapper.selectReviewPhotos(reviewId));


        // response 작성
        ReviewUpdateResponse response = new ReviewUpdateResponse();
        response.setUpdatedReview(updated);
        response.setReviewUpdated(reviewUpdated);
        response.setDeletePhotoIds(deletedPhotoIds);
        response.setNewPhotos(addedPhotos);

        return response;
        //return updated;

    }
}
