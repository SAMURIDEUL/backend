package com.example.samuL.category.controller;

import com.example.samuL.category.dto.CategoryDto;
import com.example.samuL.category.service.CategoryService;
import com.example.samuL.common.okResponse.OkResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "카테고리 목록 조회", description = "카테고리 목록을 반환합니다. /sort_order은 db용으로 id와 같습니다.")
    @GetMapping
    public ResponseEntity<OkResponse<List<CategoryDto>>> getAll(HttpServletRequest request) {
        List<CategoryDto> categoryDtos = categoryService.findAll();
        String path = request.getRequestURI();
        return ResponseEntity.ok(OkResponse.success("카테고리 목록 조회 성공", categoryDtos, path));
    }

}
