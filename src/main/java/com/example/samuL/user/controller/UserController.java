package com.example.samuL.user.controller;

import com.example.samuL.auth.jwt.JwtTokenProvider;
import com.example.samuL.common.exception.custom.DuplicateException;
import com.example.samuL.user.service.UserService;
import com.example.samuL.user.dto.ChangePasswordRequestDto;
import com.example.samuL.user.dto.MyInfoDto;
import com.example.samuL.user.dto.UpdateUserDto;
import com.example.samuL.user.dto.UserDto;
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

        if(isDuplicate){
            throw new DuplicateException("이메일이 중복되어 사용이 불가능 합니다.");
        }

        response.put("email", email);
        response.put("isDupicate", isDuplicate);
        response.put("message", "사용 가능한 이메일입니다.");
        return ResponseEntity.ok(response);
    }

    //닉네임 중복 체크
    @PostMapping("/check-nickname")
    public ResponseEntity<Map<String, Object>> checkNickname(@RequestBody Map<String, String> request){

        String nickname = request.get("nickname");

        boolean isDuplicate = userService.isNicknameDuplicate(nickname);

        Map<String, Object> response = new HashMap<>();


        if(isDuplicate){
            throw new DuplicateException("닉네임이 중복되어 사용 불가능합니다.");
        }
        response.put("nickname", nickname);
        response.put("isDuplicate", isDuplicate);
        response.put("message", "사용 가능한 닉네임 입니다.");
        return ResponseEntity.ok(response);

    }

    // 회원가입
    @PostMapping("/signup")
    public String signup(@RequestBody UserDto userDto){
        userService.signupUser(userDto);
        return "회원가입에 성공했습니다.";

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

        userService.changePassword(token, changePasswordRequestDto);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다. 다시 로그인해주세요");

    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String authorizationHeader){
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 헤더");
        }
        String accessToken = authorizationHeader.substring(7);
        userService.deleteUser(accessToken);
        return ResponseEntity.ok("회원 탈퇴 완료");
    }
}

