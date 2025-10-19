package com.example.samuL.category.service;

import com.example.samuL.category.dto.CategoryDto;
import com.example.samuL.category.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(){
        return categoryMapper.selectAll();
    }
}
