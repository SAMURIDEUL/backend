package com.example.samuL.user.controller;

import com.example.samuL.dto.*;
import com.example.samuL.auth.jwt.JwtTokenProvider;
import com.example.samuL.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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


    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, Authentication authentication){
        Map<String, String> response = new HashMap<>();
        String token = jwtTokenProvider.resolveToken(request);


//        if(authentication == null){
//            response.put("error", "Authentication is missing");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); //401
//        }

        String currentEmail = authentication.getName();


        userService.logout(token, currentEmail);
        response.put("message", "로그아웃 성공");
        return ResponseEntity.ok(response);
    }

    // 회원정보 조회
    @GetMapping("/info")
    public ResponseEntity<MyInfoDto> getMyInfo(Authentication authentication){
        String email = authentication.getName();
        MyInfoDto myInfoDto = userService.getMyInfoByEmail(email);
        return ResponseEntity.ok(myInfoDto);
    }


    // 회원정보 수정
    @PutMapping("/me")
    public ResponseEntity<?> updateMyInfo(
            @RequestBody UpdateUserDto updateUserDto,
            Authentication authentication
    ){
        String email = authentication.getName();
        userService.updateUser(email, updateUserDto);

        return ResponseEntity.ok("회원정보가 수정 되었습니다.");
    }

    // 비밀번호 수정
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(HttpServletRequest request, @RequestBody ChangePasswordRequestDto changePasswordRequestDto){
        String token = jwtTokenProvider.resolveToken(request);
        if(token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없습니다");
        }

        userService.changePassword(token, changePasswordRequestDto);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다. 다시 로그인해주세요");

    }



    // 회원 탈퇴



}

