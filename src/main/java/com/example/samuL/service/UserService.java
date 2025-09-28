package com.example.samuL.service;

import com.example.samuL.dto.*;

public interface UserService {
    public void signupUser(UserDto userDto);
    public boolean isEmailDuplicate(String email);
    public boolean isNicknameDuplicate(String nickname);
    public String login(LoginRequestDto loginRequestDto);
    public MyInfoDto getMyInfoByEmail(String email);
    public void updateUser(String email, UpdateUserDto updateUserDto);
    public void changePassword(String token, ChangePasswordRequestDto changePasswordRequestDto);
}
