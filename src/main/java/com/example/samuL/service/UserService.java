package com.example.samuL.service;

import com.example.samuL.dto.LoginRequestDto;
import com.example.samuL.dto.MyInfoDto;
import com.example.samuL.dto.UpdateUserDto;
import com.example.samuL.dto.UserDto;

public interface UserService {
    public void signupUser(UserDto userDto);
    public boolean isEmailDuplicate(String email);
    public boolean isNicknameDuplicate(String nickname);
    public String login(LoginRequestDto loginRequestDto);
    public MyInfoDto getMyInfoByEmail(String email);
    public void updateUser(String email, UpdateUserDto updateUserDto);
}
