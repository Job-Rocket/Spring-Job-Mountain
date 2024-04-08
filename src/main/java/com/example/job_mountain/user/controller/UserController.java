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

//    @GetMapping("/signup")
//    public String signup(UserCreateForm userCreateForm) {
//        return "signup_form";
//    }
//
//    @PostMapping("/signup")
//    public ResponseEntity<Object> signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "signup_form";
//        }
//
//        if (!userCreateForm.getPw().equals(userCreateForm.getPw2())) {
//            bindingResult.rejectValue("pw2", "passwordInCorrect",
//                    "2개의 패스워드가 일치하지 않습니다.");
//            return new ResponseEntity<>(userService.signup(signupUser), HttpStatus.OK);
//        }
//
//        try {
//            userService.create(userCreateForm.getUserName(), userCreateForm.getId(),
//                    userCreateForm.getPw(), userCreateForm.getEmail(), userCreateForm.getAge(), userCreateForm.getInterest());
//        } catch (DataIntegrityViolationException e) {
//            e.printStackTrace();
//            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
//            return new ResponseEntity<>(userService.signup(signupUser), HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            bindingResult.reject("signupFailed", e.getMessage());
//            return new ResponseEntity<>(userService.signup(signupUser), HttpStatus.OK);
//        }
//
//        return new ResponseEntity<>(userService.signup(signupUser), HttpStatus.OK);
//    }

    // 회원가입
//    @PostMapping("/auth/signup")
//    public Object signup(@RequestBody UserDto.SignupUser signupUser) {
//        return new ResponseEntity<>(userService.signup(signupUser), HttpStatus.OK);
//    }

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
    // 로그아웃
    @DeleteMapping("/auth/logout")
    public ResponseEntity<Object> logout(@RequestBody UserDto.LogoutUser logoutUser) {
        return new ResponseEntity<>(userService.logout(logoutUser), HttpStatus.OK);
    }

    // 프로필 조회
    @GetMapping("/auth/profile/{userId}")
    public ResponseEntity<Object> getUsersInfo(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getUserInfo(userId), HttpStatus.OK);
    }

    // 프로필 수정
    @PatchMapping("/user/update")
    public ResponseEntity<Object> fixUserInfo(@CurrentUser UserPrincipal userPrincipal,
                                              @RequestBody UserDto.UpdateUser updateUser) {
        return new ResponseEntity<>(userService.updateUser(userPrincipal, updateUser, null), HttpStatus.OK);
    }

}