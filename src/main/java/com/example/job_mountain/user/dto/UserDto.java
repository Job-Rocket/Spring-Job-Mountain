package com.example.job_mountain.user.dto;

import com.example.job_mountain.config.ResponseType;
import com.example.job_mountain.user.domain.SiteUser;
import com.example.job_mountain.validation.ExceptionCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class UserDto {
    // 회원가입
    @Getter
    public static class SignupUser {
        String id;
        String pw;
        String userName;
        String email;
        Integer age;
        List<String> interest;
        // String image;
        String imagePath; // 추가
    }

    @Getter
    public static class TokenResponse extends ResponseType {
        TokenDto tokenDto;

        public TokenResponse(ExceptionCode exceptionCode, TokenDto tokenDto) {
            super(exceptionCode);
            this.tokenDto = tokenDto;
        }
    }

    @Getter
    public static class DuplicateUserResponse extends ResponseType { // 회원가입시 ID, Email 중복, 로그인시 ID, PW 오류를 반환하기 위함
        public DuplicateUserResponse(ExceptionCode exceptionCode) {
            super(exceptionCode);
        }
    }

    @Getter
    public static class UserResponse extends ResponseType {

        @JsonInclude(NON_NULL)
        Long userId;

        @JsonInclude(NON_NULL)
        List<SiteUser> users;

        public UserResponse(ExceptionCode exceptionCode) {
            super(exceptionCode);
        }

        public UserResponse(ExceptionCode exceptionCode, Long userId) {
            super(exceptionCode);
            this.userId = userId;
        }

        public UserResponse(ExceptionCode exceptionCode, List<SiteUser> users) {
            super(exceptionCode);
            this.users = users;
        }
    }

    // 로그인
    @Getter
    public static class LoginUser {
        String id;
        String pw;
    }

    @Getter
    public static class LoginResponse extends ResponseType {
        Long userId;
        String userName;
        TokenDto tokenDto;

        public LoginResponse(ExceptionCode exceptionCode, SiteUser user, TokenDto tokenDto) {
            super(exceptionCode);
            this.userId = user.getUserId();
            this.userName = user.getUserName();
            this.tokenDto = tokenDto;
        }
    }

    // 로그아웃
    @Getter
    public static class LogoutUser {
        Long userId;
        String accessToken;
    }
    @Getter
    public static class LogoutResponse extends ResponseType {
        public LogoutResponse(ExceptionCode exceptionCode) {
            super(exceptionCode);
        }
    }

    // 프로필 조회
    @Getter
    public static class UserInfoResponse extends ResponseType {
        Long userId;
        String userName;
        String id;
        String pw;
        String email;
        String imagePath;
        Integer age;
        List<String> interest;

        public UserInfoResponse(ExceptionCode exceptionCode, SiteUser user) {
            super(exceptionCode);
            this.userId = user.getUserId();
            this.userName = user.getUserName();
            this.id = user.getId();
            this.pw = user.getPw();
            this.email = user.getEmail();
            this.imagePath = user.getImagePath();
            this.age = user.getAge();
            this.interest = user.getInterest();
        }
    }

    // 프로필 수정
    @Getter
    public static class UpdateUser {
        String id;
        String pw;
        Integer age;
        // String image;
        String imagePath; // 추가
        List<String> interest;
    }



}
