package com.example.samuL.user.controller;

import com.example.samuL.auth.jwt.JwtTokenProvider;
import com.example.samuL.common.exception.custom.DuplicateException;
import com.example.samuL.common.exception.custom.HeaderException;
import com.example.samuL.common.okResponse.OkResponse;
import com.example.samuL.review.dto.ReviewPaginatedResponse;
import com.example.samuL.review.dto.ReviewResponse;
import com.example.samuL.review.service.ReviewService;
import com.example.samuL.user.service.CustomUserDetails;
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

import java.net.HttpRetryException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    private final ReviewService reviewService;

  //  private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    // 이메일 중복 체크
    @PostMapping("/check-email")
    public ResponseEntity<OkResponse<Map<String, Object>>> checkEmail(@RequestBody Map<String, String> request, HttpServletRequest req){
        String email = request.get("email");

        boolean isDuplicate = userService.isEmailDuplicate(email);

        Map<String, Object> response = new HashMap<>();

        if(isDuplicate){
            throw new DuplicateException("이메일이 중복되어 사용이 불가능 합니다.");
        }

        response.put("email", email);
        response.put("isDupicate", isDuplicate);
        return ResponseEntity.ok(OkResponse.success("사용 가능한 이메일 입니다.", response, req.getRequestURI()));
    }

    //닉네임 중복 체크
    @PostMapping("/check-nickname")
    public ResponseEntity<OkResponse<Map<String, Object>>> checkNickname(@RequestBody Map<String, String> request, HttpServletRequest req){

        String nickname = request.get("nickname");

        boolean isDuplicate = userService.isNicknameDuplicate(nickname);

        Map<String, Object> response = new HashMap<>();


        if(isDuplicate){
            throw new DuplicateException("닉네임이 중복되어 사용 불가능합니다.");
        }
        response.put("nickname", nickname);
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(OkResponse.success("사용 가능한 닉네임 입니다.", response, req.getRequestURI()));

    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<OkResponse<Void>> signup(@RequestBody UserDto userDto, HttpServletRequest request){
        userService.signupUser(userDto);
        return ResponseEntity.ok(OkResponse.success("회원가입 성공", request.getRequestURI()));

    }


    // 회원정보 조회
    @GetMapping("/info")
    public ResponseEntity<OkResponse<MyInfoDto>> getMyInfo(Authentication authentication, HttpServletRequest request){
        String email = authentication.getName();
        MyInfoDto myInfoDto = userService.getMyInfoByEmail(email);
        return ResponseEntity.ok(OkResponse.success("회원 조회 성공", myInfoDto, request.getRequestURI()));
    }


    // 회원정보 수정
    @PutMapping("/me")
    public ResponseEntity<OkResponse<Void>> updateMyInfo(
            @RequestBody UpdateUserDto updateUserDto,
            Authentication authentication,
            HttpServletRequest request
    ){
        String email = authentication.getName();
        userService.updateUser(email, updateUserDto);

        return ResponseEntity.ok(OkResponse.success("회원정보가 수정되었습니다.", request.getRequestURI()));
    }

    // 비밀번호 수정
    @PutMapping("/change-password")
    public ResponseEntity<OkResponse<Void>> changePassword(HttpServletRequest request, @RequestBody ChangePasswordRequestDto changePasswordRequestDto){
        String token = jwtTokenProvider.resolveToken(request);

        userService.changePassword(token, changePasswordRequestDto);
        return ResponseEntity.ok(OkResponse.success("비밀번호가 성공적으로 변경되었습니다. 다시 로그인해주세요", request.getRequestURI()));

    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<OkResponse<Void>> deleteUser(@RequestHeader("Authorization") String authorizationHeader, HttpServletRequest request){
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            throw new HeaderException("잘못된 헤더입니다.");
        }
        String accessToken = authorizationHeader.substring(7);
        userService.deleteUser(accessToken);
        return ResponseEntity.ok(OkResponse.success("회원 탈퇴 완료", request.getRequestURI()));
    }

    @GetMapping("/reviews")
    public ResponseEntity<ReviewPaginatedResponse<ReviewResponse>> getMyReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ){
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();

        ReviewPaginatedResponse<ReviewResponse> reviews = reviewService.getUserReviews(userId, page, size);
        return ResponseEntity.ok(reviews);
    }
}

