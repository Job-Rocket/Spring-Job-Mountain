package com.example.job_mountain.user.service;

import com.example.job_mountain.common.RedisDao;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final RedisDao redisDao;


//    public SiteUser create(String userName, String id, String pw, String email, Integer age, List<String> interest) {
//        SiteUser user = new SiteUser();
//        user.setUserName(userName);
//        user.setId(id);
//        user.setPw(passwordEncoder.encode(pw));
//        user.setEmail(email);
//        user.setAge(age);
//        user.setInterest(interest);
//
//        this.userRepository.save(user);
//        return user;
//    }
    // 회원가입
    public TokenDto createToken(Authentication authentication, Long userId) {

        String accessToken = tokenProvider.createToken(authentication, Boolean.FALSE); // access
        String refreshToken = tokenProvider.createToken(authentication, Boolean.TRUE); // refresh

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

//    public Object signup(UserDto.SignupUser signupUser) {
//
//        Optional<SiteUser> findUser = userRepository.findById(signupUser.getId());
//        if (findUser.isPresent()) {
//            return new UserDto.DuplicateUserResponse(ExceptionCode.SIGNUP_DUPLICATED_ID); // ID 중복
//        }
//
//        findUser = userRepository.findByEmail(signupUser.getEmail());
//        if (findUser.isPresent()) {
//            return new UserDto.DuplicateUserResponse(ExceptionCode.SIGNUP_DUPLICATED_EMAIL); // Email 중복
//        }
//
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        SiteUser user = new SiteUser(signupUser, encoder.encode(signupUser.getPw()));
//        userRepository.save(user);
//
//        return new UserDto.UserResponse(ExceptionCode.SIGNUP_CREATED_OK);
//    }

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

        try {
            // 파일 저장 로직
            String uploadDir = "src/main/resources/static/upload"; // 상대 경로 사용
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            imageFile.transferTo(filePath);

            // 저장된 이미지 파일 경로를 user 객체에 설정
            user.setImagePath(uploadDir + "/" + fileName);

            userRepository.save(user);
        } catch (IOException e) {
            // IOException 처리 로직
            e.printStackTrace();
            // return new ResponseEntity<>("파일 저장 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            return new UserDto.UserResponse(ExceptionCode.FILE_STORAGE_ERROR);
        }

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
//    @CacheEvict(cacheNames = CacheNames.USERBYUSERID, key = "'login'+#p1")
//    @Transactional
    public Object logout(UserDto.LogoutUser logoutUser) {

        Long expiration = tokenProvider.getExpiration(logoutUser.getRefreshToken());
        redisDao.setBlackList(logoutUser.getRefreshToken(), "logout", expiration);
        if (redisDao.hasKey(String.valueOf(logoutUser.getUserId()))) {
            redisDao.deleteRefreshToken(String.valueOf(logoutUser.getUserId()));
        } else {
           // throw new IllegalArgumentException("이미 로그아웃한 유저입니다.");
            return new UserDto.LogoutResponse(ExceptionCode.LOGOUT_INVALID);
        }
        // return ResponseEntity.ok("로그아웃 완료");
         return new UserDto.LogoutResponse(ExceptionCode.LOGOUT_OK);
    }


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

        Optional<SiteUser> byId = userRepository.findById(updateUser.getId()); // id 중복
        if (byId.isPresent() && ! byId.get().getUserId().equals(userPrincipal.getUserId())) {
            return new UserDto.UserResponse(ExceptionCode.SIGNUP_DUPLICATED_EMAIL);
        }

        Optional<SiteUser> byEmail = userRepository.findByEmail(updateUser.getEmail()); // email 중복
        if (byEmail.isPresent() && ! byEmail.get().getUserId().equals(userPrincipal.getUserId())) {
            return new UserDto.UserResponse(ExceptionCode.SIGNUP_DUPLICATED_EMAIL);
        }
        user.updateUser(updateUser);
        try {
            // 파일 저장 로직
            String uploadDir = "src/main/resources/static/upload"; // 상대 경로 사용
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            imageFile.transferTo(filePath);

            // 저장된 이미지 파일 경로를 user 객체에 설정
            user.setImagePath(uploadDir + "/" + fileName);

            userRepository.save(user);

        } catch (IOException e) {
            // IOException 처리 로직
            e.printStackTrace();
            // return new ResponseEntity<>("파일 저장 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            return new UserDto.UserResponse(ExceptionCode.FILE_STORAGE_ERROR);
        }

        return new UserDto.UserResponse(ExceptionCode.USER_UPDATE_OK);
    }

}
