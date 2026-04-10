package com.example.samuL.user.controller;

import com.example.samuL.auth.jwt.JwtTokenProvider;
import com.example.samuL.common.exception.custom.DuplicateException;
import com.example.samuL.common.exception.custom.HeaderException;
import com.example.samuL.common.okResponse.OkResponse;
import com.example.samuL.place.service.PlaceFavoriteService;
import com.example.samuL.review.dto.ReviewPaginatedResponse;
import com.example.samuL.review.dto.ReviewResponse;
import com.example.samuL.review.service.ReviewService;
import com.example.samuL.user.service.CustomUserDetails;
import com.example.samuL.user.service.UserService;
import com.example.samuL.user.dto.ChangePasswordRequestDto;
import com.example.samuL.user.dto.MyInfoDto;
import com.example.samuL.user.dto.UpdateUserDto;
import com.example.samuL.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

//import java.net.HttpRetryException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    private final ReviewService reviewService;
    private final PlaceFavoriteService placeFavoriteService;
    private final JwtTokenProvider jwtTokenProvider;

    // 이메일 중복 체크
    @Operation(summary = "이메일 중복 체크", description = "이메일이 중복되는지 체크합니다. isDuplicated가 false(200 ok)면 중복되는 이메일이 없다는 뜻이고 true(400 bad Request)이면 중복되는 이메일임으로 사용이 불가능합니다."
            +
            " / request body 및 response가 swagger 상에서 원래와 다르게 나옵니다. notion을 확인해 주세요)")
    @PostMapping("/check-email")
    public ResponseEntity<OkResponse<Map<String, Object>>> checkEmail(@RequestBody Map<String, String> request,
            HttpServletRequest req) {
        String email = request.get("email");

        boolean isDuplicate = userService.isEmailDuplicate(email);

        Map<String, Object> response = new HashMap<>();

        if (isDuplicate) {
            throw new DuplicateException("이메일이 중복되어 사용이 불가능 합니다.");
        }

        response.put("email", email);
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(OkResponse.success("사용 가능한 이메일 입니다.", response, req.getRequestURI()));
    }

    // 닉네임 중복 체크
    @Operation(summary = "닉네임 중복 확인", description = "닉네임이 중복되는지 확인합니다. isDuplicated가 false(200 ok)면 중복되는 닉네임이 없다는 뜻이고 true(400 bad Request)이면 중복되는 닉네임으로 사용이 불가능합니다."
            +
            " / request body 및 response가 swagger 상에서 원래와 다르게 나옵니다. notion을 확인해 주세요)")
    @PostMapping("/check-nickname")
    public ResponseEntity<OkResponse<Map<String, Object>>> checkNickname(@RequestBody Map<String, String> request,
            HttpServletRequest req) {

        String nickname = request.get("nickname");

        boolean isDuplicate = userService.isNicknameDuplicate(nickname);

        Map<String, Object> response = new HashMap<>();

        if (isDuplicate) {
            throw new DuplicateException("닉네임이 중복되어 사용 불가능합니다.");
        }
        response.put("nickname", nickname);
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(OkResponse.success("사용 가능한 닉네임 입니다.", response, req.getRequestURI()));

    }

    // 회원가입
    @Operation(summary = "회원가입", description = "회원가입, request body의 created_at, updated_at은 자동 생성됨으로 request body에 넣지 않아도 됩니다.")
    @PostMapping("/signup")
    public ResponseEntity<OkResponse<Void>> signup(@Valid @RequestBody UserDto userDto, HttpServletRequest request) {
        userService.signupUser(userDto);
        return ResponseEntity.ok(OkResponse.success("회원가입 성공", request.getRequestURI()));

    }

    // 회원정보 조회
    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회합니다.")
    @GetMapping("/info")
    public ResponseEntity<OkResponse<MyInfoDto>> getMyInfo(Authentication authentication, HttpServletRequest request) {
        String email = authentication.getName();
        MyInfoDto myInfoDto = userService.getMyInfoByEmail(email);
        return ResponseEntity.ok(OkResponse.success("회원 조회 성공", myInfoDto, request.getRequestURI()));
    }

    // 회원정보 수정
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다. /request body에 updated_at은 넣을 필요 없습니다.")
    @PutMapping("/me")
    public ResponseEntity<OkResponse<Void>> updateMyInfo(
            @Valid @RequestBody UpdateUserDto updateUserDto,
            Authentication authentication,
            HttpServletRequest request) {
        String email = authentication.getName();
        userService.updateUser(email, updateUserDto);

        return ResponseEntity.ok(OkResponse.success("회원정보가 수정되었습니다.", request.getRequestURI()));
    }

    // 비밀번호 수정
    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경합니다.")
    @PutMapping("/change-password")
    public ResponseEntity<OkResponse<Void>> changePassword(HttpServletRequest request,
            @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        String token = jwtTokenProvider.resolveToken(request);

        userService.changePassword(token, changePasswordRequestDto);
        return ResponseEntity.ok(OkResponse.success("비밀번호가 성공적으로 변경되었습니다. 다시 로그인해주세요", request.getRequestURI()));

    }

    // 회원 탈퇴
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<OkResponse<Void>> deleteUser(
            @Parameter(description = "accessToken") @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest request) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new HeaderException("잘못된 헤더입니다.");
        }
        String accessToken = authorizationHeader.substring(7);
        userService.deleteUser(accessToken);
        return ResponseEntity.ok(OkResponse.success("회원 탈퇴 완료", request.getRequestURI()));
    }

    @Operation(summary = "자신이 적은 리뷰 조회", description = "자신이 적은 리뷰를 조회합니다.")
    @GetMapping("/reviews")
    public ResponseEntity<OkResponse<ReviewPaginatedResponse<ReviewResponse>>> getMyReviews(
            @Parameter(description = "현재 페이지, 만약 hasNext가 true라면 다음 페이지가 있다는 뜻으로 현재 페이지 + 1을 하면 다음 페이지") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "한 번에 가져올 리뷰들의 개수") @RequestParam(defaultValue = "10") int size,
            Authentication authentication,
            HttpServletRequest request) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();

        ReviewPaginatedResponse<ReviewResponse> reviews = reviewService.getUserReviews(userId, page, size);
        return ResponseEntity.ok(OkResponse.success("자신이 적은 리뷰 조회 성공", reviews, request.getRequestURI()));
    }

    @Operation(summary = "자신의 찜 조회", description = "사용자가 찜한 placeId를 조회합니다.")
    @GetMapping("/likes")
    public ResponseEntity<OkResponse<List<Long>>> getMyFavoritePlacesFor(
            Authentication authentication,
            HttpServletRequest request) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        List<Long> favoritePlaceIds = placeFavoriteService.getMyFavoritePlaceIds(userId);
        String path = request.getRequestURI();
        return ResponseEntity.ok(OkResponse.success(favoritePlaceIds, path));
    }

}
