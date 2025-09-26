package com.example.samuL.controller;

import com.example.samuL.dto.LoginRequestDto;
import com.example.samuL.dto.LoginResponseDto;
import com.example.samuL.dto.MyInfoDto;
import com.example.samuL.dto.UserDto;
import com.example.samuL.jwt.JwtTokenProvider;
import com.example.samuL.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    // 이메일 중복 체크
    @PostMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestBody Map<String, String> request){
        String email = request.get("email");

        boolean isDuplicate = userService.isEmailDuplicate(email);

        Map<String, Object> response = new HashMap<>();
        response.put("email", email);
        response.put("isDupicate", isDuplicate);

        if(isDuplicate){
            response.put("message", "Email is already registered");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put("message", "Email is available");
        return ResponseEntity.ok(response);
    }

    //닉네임 중복 체크
    @PostMapping("/check-nickname")
    public ResponseEntity<Map<String, Object>> checkNickname(@RequestBody Map<String, String> request){

        String nickname = request.get("nickname");

        boolean isDuplicate = userService.isNicknameDuplicate(nickname);

        Map<String, Object> response = new HashMap<>();
        response.put("nickname", nickname);
        response.put("isDuplicate", isDuplicate);

        if(isDuplicate){
            response.put("message", "Nickname is already registered");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put("message", "Nickname is available");
        return ResponseEntity.ok(response);

    }

    // 회원가입
    @PostMapping("/signup")
    public String signup(@RequestBody UserDto userDto){
        userService.signupUser(userDto);
        return "User registration successful";

    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword_hash())
        );
        String token = jwtTokenProvider.CreateToken(requestDto.getEmail());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    // 로그아웃


    // 회원정보 조회
    @GetMapping("/info")
    public ResponseEntity<MyInfoDto> getMyInfo(Authentication authentication){
        String email = authentication.getName();
        MyInfoDto myInfoDto = userService.getMyInfoByEmail(email);
        return ResponseEntity.ok(myInfoDto);
    }


    // 회원정보 수정



    // 회원 탈퇴



}

