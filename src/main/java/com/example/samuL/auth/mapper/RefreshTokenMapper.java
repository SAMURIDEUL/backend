package com.example.samuL.auth.mapper;


import com.example.samuL.auth.dto.RefreshTokenDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefreshTokenMapper {

    public void insert(RefreshTokenDto refreshTokenDto);
    public void deleteByEmail(String email);
    public RefreshTokenDto findByEmail(String email);
}
