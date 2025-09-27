package com.example.samuL.service;

import com.example.samuL.dto.LoginRequestDto;
import com.example.samuL.dto.MyInfoDto;
import com.example.samuL.dto.UpdateUserDto;
import com.example.samuL.dto.UserDto;
import com.example.samuL.jwt.JwtTokenProvider;
import com.example.samuL.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    //회원가입
    @Override
    public void signupUser(UserDto userDto){
        userDto.setPassword_hash(passwordEncoder.encode(userDto.getPassword_hash()));
        userMapper.signupUser(userDto);
    }

    //이메일 중복 확인
    @Override
    public boolean isEmailDuplicate(String email){
        return userMapper.countEmail(email) > 0;
    }

    // 닉네임 중복 확인
    @Override
    public boolean isNicknameDuplicate(String nickname){
        return userMapper.countNickname(nickname) > 0;
    }


    // 로그인
    @Override
    public String login(LoginRequestDto loginRequestDto){
        UserDto userDto = userMapper.findByEmail(loginRequestDto.getEmail());
        if(userDto == null || !passwordEncoder.matches(loginRequestDto.getPassword_hash(), userDto.getPassword_hash())){
            throw new RuntimeException("Invalid email or password");
        }
        return jwtTokenProvider.CreateToken(userDto.getEmail());
    }

    // 회원정보 조회
    @Override
    public MyInfoDto getMyInfoByEmail(String email){
        UserDto userDto = userMapper.findByEmail(email);
        if(userDto == null){
            throw new UsernameNotFoundException("Email not found");
        }
        return new MyInfoDto(userDto.getEmail(), userDto.getNickname(), userDto.getCreated_at(), userDto.getUpdated_at());
    }

    // 회원 정보 수정
    @Override
    public void updateUser(String email, UpdateUserDto updateUserDto){
        UserDto userDto = userMapper.findByEmail(email);
        if(userDto == null){
            throw new UsernameNotFoundException("Email not found");
        }

        userDto.setNickname(updateUserDto.getNickname());
        userDto.setUpdated_at(LocalDateTime.now().withNano(0));
        userMapper.updateUser(userDto);
    }


}
