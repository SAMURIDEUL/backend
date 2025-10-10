package com.example.samuL.user.service;

import com.example.samuL.user.dto.ChangePasswordRequestDto;
import com.example.samuL.user.dto.MyInfoDto;
import com.example.samuL.user.dto.UpdateUserDto;
import com.example.samuL.user.dto.UserDto;

public interface UserService {
    public void signupUser(UserDto userDto);
    public boolean isEmailDuplicate(String email);
    public boolean isNicknameDuplicate(String nickname);
    public MyInfoDto getMyInfoByEmail(String email);
    public void updateUser(String email, UpdateUserDto updateUserDto);
    public void changePassword(String token, ChangePasswordRequestDto changePasswordRequestDto);
    public void deleteUser(String accesstoken);

}
