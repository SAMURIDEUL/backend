package com.example.samuL.mapper;

import com.example.samuL.dto.PasswordUpdateDto;
import com.example.samuL.dto.UpdateUserDto;
import com.example.samuL.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public void signupUser(UserDto userDto);
    public int countEmail(String email);
    public int countNickname(String nickname);
    public UserDto findByEmail(String email);
    public int updateUser(UserDto userDto);
    public int updatePassword(PasswordUpdateDto passwordUpdateDto);
}
