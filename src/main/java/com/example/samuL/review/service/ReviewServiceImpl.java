package com.example.samuL.review.service;

import com.example.samuL.common.exception.custom.FileUploadsException;
import com.example.samuL.common.exception.custom.ReviewValidationException;
import com.example.samuL.common.properties.FileStorageProperties;
import com.example.samuL.review.dto.*;
import com.example.samuL.review.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
        if(reviewDto.getContent() == null || reviewDto.getContent().length() < 5){
            throw new ReviewValidationException("리뷰 내용은 최소 5자 이상이어야 합니다.");
        }

        if(reviewDto.getRating() < 1 || reviewDto.getRating() > 5){
            throw new ReviewValidationException("평점은 1점에서 5점 사이여야 합니다.");
        }
        reviewDto.setUserId(userId);
        reviewMapper.insertReview(reviewDto);

        Path uploadDir = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        Files.createDirectories(uploadDir);

        if(imageFiles != null && !imageFiles.isEmpty()){
            for(MultipartFile file : imageFiles){
                if(file.isEmpty()){
                    throw new FileUploadsException("빈 파일은 업로드할 수 없습니다.");
                }

                String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
                if(ext == null || (!ext.equalsIgnoreCase("jpg")
                        && !ext.equalsIgnoreCase("jpeg")
                        && !ext.equalsIgnoreCase("png"))){
                    throw new FileUploadsException("지원하지 않는 이미지 형식입니다. (jpg, jpeg, png)");
                }
                String filename = UUID.randomUUID() + (ext != null ? "." + ext : "");

                Path filePath = uploadDir.resolve(filename);
                try{
                    // 파일 저장
                    file.transferTo(filePath.toFile());
                }catch(IOException e){
                    throw new FileUploadsException("이미지 저장 중 오류가 발생했습니다.");
                }

                //System.out.println("파일 저장 경로: " + filePath.toAbsolutePath());
                // DB 저장
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

    @Override
    @Transactional
    public void deleteReview(Long reviewId, Long userId){
        Long ownerId = reviewMapper.findReviewOwner(reviewId);
        if (ownerId == null){
            throw new IllegalArgumentException("존재하지 않는 리뷰입니다.");
        }
        if(!ownerId.equals(userId)){
            throw new AccessDeniedException("본인 리뷰만 삭제할 수 있습니다.");
        }
        reviewMapper.deleteReview(reviewId);
    }

    // 자신이 작성한 리뷰 조회
    @Override
    public ReviewPaginatedResponse<ReviewResponse> getUserReviews(Long userId, int page, int size){
        int offset = page * size;

        // 리뷰 조회
        List<ReviewResponse> reviews = reviewMapper.getUserReviews(userId, offset, size);

        if (!reviews.isEmpty()){
            List<Long> reviewIds = reviews.stream().map(ReviewResponse::getId).toList();

            // 사진 조회
            List<ReviewPhotoDto> photos = reviewMapper.getPhotosByReviewIds(reviewIds);
            // Map으로 매핑
            Map<Long, List<String>> photoMap = photos.stream().collect(Collectors.groupingBy(ReviewPhotoDto::getReviewId,
                    Collectors.mapping(ReviewPhotoDto::getPhotoUrl, Collectors.toList())));
            // 리뷰에 사진 연결
            reviews.forEach(r->r.setPhotoUrls(photoMap.getOrDefault(r.getId(), List.of())));
        }

        long total = reviewMapper.countUserReviews(userId);
        int totalPages = (int)Math.ceil((double) total / size);

        return new ReviewPaginatedResponse<>(reviews, page, size, total, totalPages);

    }
}