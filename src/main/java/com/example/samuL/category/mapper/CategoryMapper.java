package com.example.samuL.category.mapper;

import com.example.samuL.category.dto.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<CategoryDto> selectAll();
}
