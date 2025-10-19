package com.example.samuL.category.controller;


import com.example.samuL.auth.dto.LoginRequestDto;
import com.example.samuL.auth.dto.LoginResponseDto;
import com.example.samuL.category.dto.CategoryDto;
import com.example.samuL.category.service.CategoryService;
import com.example.samuL.common.okResponse.OkResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<OkResponse<List<CategoryDto>>> getAll(HttpServletRequest request){
        List<CategoryDto> categoryDtos = categoryService.findAll();
        String path = request.getRequestURI();
        return ResponseEntity.ok(OkResponse.success("카테고리 목록 조회 성공", categoryDtos, path));
    }



}
