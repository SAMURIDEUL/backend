package com.example.samuL.auth.mapper;


import com.example.samuL.auth.dto.LoginUserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {
    public LoginUserDto findEmail(String email);
}
