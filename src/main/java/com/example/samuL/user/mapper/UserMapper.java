package com.example.samuL.user.mapper;

import com.example.samuL.user.dto.PasswordUpdateDto;
import com.example.samuL.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public void signupUser(UserDto userDto);
    public int countEmail(String email);
    public int countNickname(String nickname);
    public UserDto findByEmail(String email);
    public int updateUser(UserDto userDto);
    public int updatePassword(PasswordUpdateDto passwordUpdateDto);
    public int deleteUser(String email);
}
