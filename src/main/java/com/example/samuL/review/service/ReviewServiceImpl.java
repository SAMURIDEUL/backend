package com.example.samuL.review.service;

import com.example.samuL.common.properties.FileStorageProperties;
import com.example.samuL.review.dto.ReviewDto;
import com.example.samuL.review.dto.ReviewPhotoDto;
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

                System.out.println("파일 저장 경로: " + filePath.toAbsolutePath());
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
}
