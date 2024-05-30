package com.example.job_mountain.user.controller;

import com.example.job_mountain.security.CurrentUser;
import com.example.job_mountain.security.UserPrincipal;
import com.example.job_mountain.user.dto.UserDto;
import com.example.job_mountain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping

public class UserController {

    private final UserService userService;

    // 회원가입(+이미지파일)
    @PostMapping("/auth/signup")
    public ResponseEntity<Object> signup(@RequestPart("signupUser") UserDto.SignupUser signupUser, @RequestPart("imageFile") MultipartFile imageFile) {
        // imagePath를 사용하여 회원가입 로직에 이미지 경로 포함
        Object signupResult = userService.signup(signupUser, imageFile);
        return new ResponseEntity<>(signupResult, HttpStatus.OK);
    }

    // 로그인
    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody UserDto.LoginUser loginUser) {
        return new ResponseEntity<>(userService.login(loginUser), HttpStatus.OK);
    }

    // 로그아웃(실행안됨)
//    @DeleteMapping("/logout")
//    public ResponseEntity<Object> logout(@RequestBody UserDto.LogoutUser logoutUser) {
//        return new ResponseEntity<>(userService.logout(logoutUser), HttpStatus.OK);
//    }

    // 로그인 후, 회원 정보 조회
    @GetMapping("/user")
    public ResponseEntity<Object> getUserInfo(@PathVariable Long userId) {
        Object response = userService.getUserInfo(userId);

        if (response instanceof UserDto.UserInfoResponse) {
            UserDto.UserInfoResponse userInfoResponse = (UserDto.UserInfoResponse) response;
            if (userInfoResponse.getUserId() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userInfoResponse);
            } else {
                return ResponseEntity.ok(userInfoResponse);
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 로그인 후, 프로필 수정
    @PatchMapping("/user")
    public ResponseEntity<Object> fixUserInfo(@CurrentUser UserPrincipal userPrincipal,
                                              @RequestPart("updateUser") UserDto.UpdateUser updateUser,
                                              @RequestPart("imageFile") MultipartFile imageFile) {
        // imagePath를 사용하여 회원가입 로직에 이미지 경로 포함
        Object updateResult = userService.updateUser(userPrincipal, updateUser, imageFile);
        return new ResponseEntity<>(updateResult, HttpStatus.OK);
    }


}