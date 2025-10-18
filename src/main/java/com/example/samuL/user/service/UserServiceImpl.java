package com.example.samuL.user.service;

import com.example.samuL.JwtBlackList.service.JwtBlacklistService;
import com.example.samuL.auth.jwt.JwtTokenProvider;
import com.example.samuL.auth.mapper.RefreshTokenMapper;
import com.example.samuL.user.mapper.UserMapper;
import com.example.samuL.user.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private JwtBlacklistService jwtBlacklistService;
    @Autowired
    private RefreshTokenMapper refreshTokenMapper;

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


    // 회원정보 조회
    @Override
    public MyInfoDto getMyInfoByEmail(String email){
        UserDto userDto = userMapper.findByEmail(email);
        if(userDto == null){
            throw new UsernameNotFoundException("이메일을 찾을 수 없습니다.");
        }
        return new MyInfoDto(userDto.getEmail(), userDto.getNickname(), userDto.getCreated_at(), userDto.getUpdated_at());
    }

    // 회원 정보 수정
    @Override
    public void updateUser(String email, UpdateUserDto updateUserDto){
        UserDto userDto = userMapper.findByEmail(email);

        String newNickname = updateUserDto.getNickname();
        if(newNickname == null || newNickname.trim().isEmpty()){
            throw new IllegalArgumentException("닉네임이 비어 있습니다.");
        }
        userDto.setNickname(updateUserDto.getNickname());
        userDto.setUpdated_at(LocalDateTime.now().withNano(0));
        userMapper.updateUser(userDto);
    }

    // 비밀번호 변경
    @Override
    public void changePassword(String token, ChangePasswordRequestDto changePasswordRequestDto){
        if(jwtBlacklistService.isTokenBlacklisted(token)){
            throw new IllegalArgumentException("블랙리스트에 등록된 토큰입니다.");
        }

        String email = jwtTokenProvider.extractEmail(token);
        UserDto userDto = userMapper.findByEmail(email);
        if(userDto == null){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        if(!passwordEncoder.matches(changePasswordRequestDto.getCurrentPassword(), userDto.getPassword_hash())){
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        String encodedNewpassword = passwordEncoder.encode(changePasswordRequestDto.getNewPassword());
        PasswordUpdateDto passwordUpdateDto = new PasswordUpdateDto();
        passwordUpdateDto.setEmail(email);
        passwordUpdateDto.setNewPassword(encodedNewpassword);
        userMapper.updatePassword(passwordUpdateDto);

        // refresh token 삭제
        refreshTokenMapper.deleteByEmail(email);

        //access token 블랙리스트 등록
        LocalDateTime expiry = jwtTokenProvider.getExpiration(token);
        jwtBlacklistService.addTokenToBlacklist(token, expiry);
    }
    // 회원 삭제
    @Override
    public void deleteUser(String accessToken){
        if(!jwtTokenProvider.isValidateToken(accessToken)){
            throw new RuntimeException("유효하지 않는 토큰입니다.");
        }
        String email = jwtTokenProvider.extractEmail(accessToken);

        // 사용자 정보(DB) 삭제
        userMapper.deleteUser(email);

        //refresh token 삭제
        refreshTokenMapper.deleteByEmail(email);

        //access token 블랙리스트 등록
        LocalDateTime expiry = jwtTokenProvider.getExpiration(accessToken);
        jwtBlacklistService.addTokenToBlacklist(accessToken, expiry);
    }



}
