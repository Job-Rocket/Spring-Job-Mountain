package com.example.job_mountain.company.service;

import com.example.job_mountain.company.domain.Company;
import com.example.job_mountain.company.dto.CompanyDto;
import com.example.job_mountain.company.repository.CompanyRepository;
import com.example.job_mountain.security.TokenProvider;
import com.example.job_mountain.user.dto.TokenDto;
import com.example.job_mountain.validation.ExceptionCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public TokenDto createToken(Authentication authentication, Long companyId) {

        String accessToken = tokenProvider.createToken(authentication, Boolean.FALSE); // access
        String refreshToken = tokenProvider.createToken(authentication, Boolean.TRUE); // refresh

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 회원가입
    public Object signup(CompanyDto.SignupCompany signupCompany, MultipartFile imageFile) {
        Optional<Company> findCompany = companyRepository.findById(signupCompany.getId());
        if (findCompany.isPresent()) {
            return new CompanyDto.DuplicateCompanyResponse(ExceptionCode.SIGNUP_DUPLICATED_ID); // ID 중복
        }

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        Company company = new Company(signupCompany, encoder.encode(signupCompany.getPw()));

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

            // 저장된 이미지 파일 경로를 company 객체에 설정
            company.setImagePath(uploadDir + "/" + fileName);

            companyRepository.save(company);
        } catch (IOException e) {
            // IOException 처리 로직
            e.printStackTrace();
            // return new ResponseEntity<>("파일 저장 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            return new CompanyDto.CompanyResponse(ExceptionCode.FILE_STORAGE_ERROR);
        }

        return new CompanyDto.CompanyResponse(ExceptionCode.SIGNUP_CREATED_OK);
    }


    // 로그인
    public Object login(CompanyDto.LoginCompany loginCompany) {

        Optional<Company> findUser = companyRepository.findById(loginCompany.getId());

        Company companyUser = findUser.get();
        System.out.println("111221");
        if (findUser.isEmpty()) {
            return new CompanyDto.DuplicateCompanyResponse(ExceptionCode.LOGIN_NOT_FOUND_ID);
        } else if (! PasswordEncoderFactories.createDelegatingPasswordEncoder().matches(loginCompany.getPw(), findUser.get().getPw())) {
            return new CompanyDto.DuplicateCompanyResponse(ExceptionCode.LOGIN_NOT_FOUND_PW);
        }

        System.out.println(loginCompany.getId() + " : " + loginCompany.getPw());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginCompany.getId(),
                        loginCompany.getPw()
                )
        );

        System.out.println("11113");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto tokenDto = createToken(authentication, companyUser.getCompanyId());
        companyUser.setToken(tokenDto.getRefreshToken());

        System.out.println("11114");
        companyRepository.save(companyUser);

        System.out.println("11115");
        return new CompanyDto.LoginResponse(ExceptionCode.LOGIN_OK, findUser.get(), tokenDto);
    }

    // 로그아웃
//    @CacheEvict(cacheNames = CacheNames.USERBYUSERID, key = "'login'+#p1")
//    @Transactional
//    public Object logout(CompanyDto.LogoutCompany logoutCompany) {
//        // 레디스에 accessToken 사용못하도록 등록
//        Long expiration = tokenProvider.getExpiration(logoutCompany.getAccessToken());
//        redisDao.setBlackList(logoutCompany.getAccessToken(), "logout", expiration);
//        if (redisDao.hasKey(String.valueOf(logoutCompany.getCompanyId()))) {
//            redisDao.deleteRefreshToken(String.valueOf(logoutCompany.getCompanyId()));
//        } else {
//            // throw new IllegalArgumentException("이미 로그아웃한 유저입니다.");
//            return new CompanyDto.LogoutResponse(ExceptionCode.LOGOUT_INVALID);
//        }
//        // return ResponseEntity.ok("로그아웃 완료");
//        return new CompanyDto.LogoutResponse(ExceptionCode.LOGOUT_OK);
//    }

    // 프로필 조회
    public Object getCompanyInfo(Long companyId) {
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        System.out.println("1111");
        if (optionalCompany.isEmpty()) {

            return new CompanyDto.CompanyInfoResponse(ExceptionCode.USER_NOT_FOUND);
        } else {
            Company company = optionalCompany.get();
            return new CompanyDto.CompanyInfoResponse(ExceptionCode.USER_FOUND, company);
        }
    }

    // 프로필 수정
    public Object updateCompany(Long companyId, CompanyDto.UpdateCompany updateCompany, MultipartFile imageFile) {

        Optional<Company> findCompany = companyRepository.findByCompanyId(companyId);

        System.out.println(findCompany.get().toString());

        System.out.println("1111");
        if (findCompany.isEmpty()) {
            return new CompanyDto.CompanyResponse(ExceptionCode.USER_NOT_FOUND);
        }
        Company company = findCompany.get();

//        Optional<Company> byId = companyRepository.findById(updateCompany.getId()); // id 중복
//        if (byId.isPresent() && ! byId.get().getCompanyId().equals(companyId)) {
//            return new CompanyDto.CompanyResponse(ExceptionCode.SIGNUP_DUPLICATED_ID);
//        }

        company.updateCompany(updateCompany);
        //추가
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        company.setPw(encoder.encode(updateCompany.getPw()));
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
            company.setImagePath(uploadDir + "/" + fileName);

            companyRepository.save(company);

        } catch (IOException e) {
            // IOException 처리 로직
            e.printStackTrace();
            // return new ResponseEntity<>("파일 저장 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            return new CompanyDto.CompanyResponse(ExceptionCode.FILE_STORAGE_ERROR);
        }

        return new CompanyDto.CompanyResponse(ExceptionCode.USER_UPDATE_OK);
    }
}
