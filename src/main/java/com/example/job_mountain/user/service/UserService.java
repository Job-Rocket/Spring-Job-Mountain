package com.example.job_mountain.user.service;

import com.example.job_mountain.file.FileService;
import com.example.job_mountain.security.TokenProvider;
import com.example.job_mountain.security.UserPrincipal;
import com.example.job_mountain.user.domain.SiteUser;
import com.example.job_mountain.user.dto.TokenDto;
import com.example.job_mountain.user.dto.UserDto;
import com.example.job_mountain.user.repository.UserRepository;
import com.example.job_mountain.validation.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final FileService fileService;


    public TokenDto createToken(Authentication authentication, Long userId) {

        String accessToken = tokenProvider.createToken(authentication, Boolean.FALSE); // access
        String refreshToken = tokenProvider.createToken(authentication, Boolean.TRUE); // refresh

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 회원가입
    public Object signup(UserDto.SignupUser signupUser, MultipartFile imageFile) {
        Optional<SiteUser> findUser = userRepository.findById(signupUser.getId());
        if (findUser.isPresent()) {
            return new UserDto.DuplicateUserResponse(ExceptionCode.SIGNUP_DUPLICATED_ID); // ID 중복
        }

        findUser = userRepository.findByEmail(signupUser.getEmail());
        if (findUser.isPresent()) {
            return new UserDto.DuplicateUserResponse(ExceptionCode.SIGNUP_DUPLICATED_EMAIL); // Email 중복
        }

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        SiteUser user = new SiteUser(signupUser, encoder.encode(signupUser.getPw()));

        if (imageFile != null && !imageFile.isEmpty()) {
            String image = fileService.saveFile(user.getUserId(), imageFile, "user");
            user.setImagePath(image);
        } else {
            user.setImagePath(null);
        }
        userRepository.save(user);
        return new UserDto.UserResponse(ExceptionCode.SIGNUP_CREATED_OK);
    }


    // 로그인
     public Object login(UserDto.LoginUser loginUser) {

         Optional<SiteUser> findUser = userRepository.findById(loginUser.getId());

         if (findUser.isEmpty()) {
             return new UserDto.DuplicateUserResponse(ExceptionCode.LOGIN_NOT_FOUND_ID);
         } else if (! PasswordEncoderFactories.createDelegatingPasswordEncoder().matches(loginUser.getPw(), findUser.get().getPw())) {
             return new UserDto.DuplicateUserResponse(ExceptionCode.LOGIN_NOT_FOUND_PW);
         }

         Authentication authentication = authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(
                         findUser.get().getId(),
                         loginUser.getPw()
                 )
         );

         SecurityContextHolder.getContext().setAuthentication(authentication);

         TokenDto tokenDto = createToken(authentication, findUser.get().getUserId());
         findUser.get().setToken(tokenDto.getRefreshToken());
         userRepository.save(findUser.get());

         return new UserDto.LoginResponse(ExceptionCode.LOGIN_OK, findUser.get(), tokenDto);
     }

    // 로그아웃
//    public Object logout(UserDto.LogoutUser logoutUser) {
//
//        Long expiration = tokenProvider.getExpiration(logoutUser.getRefreshToken());
//        redisDao.setBlackList(logoutUser.getRefreshToken(), "logout", expiration);
//        if (redisDao.hasKey(String.valueOf(logoutUser.getUserId()))) {
//            redisDao.deleteRefreshToken(String.valueOf(logoutUser.getUserId()));
//        } else {
//           // throw new IllegalArgumentException("이미 로그아웃한 유저입니다.");
//            return new UserDto.LogoutResponse(ExceptionCode.LOGOUT_INVALID);
//        }
//        // return ResponseEntity.ok("로그아웃 완료");
//         return new UserDto.LogoutResponse(ExceptionCode.LOGOUT_OK);
//    }

    // 프로필 조회
    public SiteUser findByUserId(Long userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음"));
    }
    public Object getUserInfo(Long userId) {
        Optional<SiteUser> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return new UserDto.UserInfoResponse(ExceptionCode.USER_NOT_FOUND);
        } else {
            SiteUser user = optionalUser.get();
            return new UserDto.UserInfoResponse(ExceptionCode.USER_FOUND, user);
        }
    }


    // 프로필 수정
    public Object updateUser(UserPrincipal userPrincipal, UserDto.UpdateUser updateUser, MultipartFile imageFile) {

        Optional<SiteUser> findUser = userRepository.findByUserId(userPrincipal.getUserId());
        if (findUser.isEmpty()) {
            return new UserDto.UserResponse(ExceptionCode.USER_NOT_FOUND);
        }
        SiteUser user = findUser.get();
        user.updateUser(updateUser);

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPw(encoder.encode(updateUser.getPw()));

        // 회원정보나 파일 중 하나라도 있는 경우만 게시글을 저장함
        if (imageFile != null && !imageFile.isEmpty()) {
            String image = fileService.saveFile(user.getUserId(), imageFile, "user");
            user.setImagePath(image);
        } else {
            user.setImagePath(null);
        }
        userRepository.save(user);

        return new UserDto.UserResponse(ExceptionCode.USER_UPDATE_OK);
    }

}
