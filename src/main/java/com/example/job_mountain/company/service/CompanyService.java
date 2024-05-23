package com.example.job_mountain.company.service;

import com.example.job_mountain.company.domain.Company;
import com.example.job_mountain.company.dto.CompanyDto;
import com.example.job_mountain.company.repository.CompanyRepository;
import com.example.job_mountain.file.FileService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final FileService fileService;

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

        if(imageFile != null&& !imageFile.isEmpty()){
            String image = fileService.saveFile(company.getCompanyId(),imageFile,"company");
            company.setImagePath(image);
        }else{
            company.setImagePath(null);
        }
        companyRepository.save(company);
        return new CompanyDto.CompanyResponse(ExceptionCode.SIGNUP_CREATED_OK);
    }


    // 로그인
    public Object login(CompanyDto.LoginCompany loginCompany) {
        Optional<Company> findUser = companyRepository.findById(loginCompany.getId());

        if (findUser.isEmpty()) {
            return new CompanyDto.DuplicateCompanyResponse(ExceptionCode.LOGIN_NOT_FOUND_ID);
        }

        Company companyUser = findUser.get();

        if (!PasswordEncoderFactories.createDelegatingPasswordEncoder().matches(loginCompany.getPw(), findUser.get().getPw())) {
            return new CompanyDto.DuplicateCompanyResponse(ExceptionCode.LOGIN_NOT_FOUND_PW);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginCompany.getId(),
                        loginCompany.getPw()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto tokenDto = createToken(authentication, companyUser.getCompanyId());
        companyUser.setToken(tokenDto.getRefreshToken());
        companyRepository.save(companyUser);
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
    public Company findByCompanyId(Long companyId){
        return companyRepository.findByCompanyId(companyId).orElseThrow(()->new RuntimeException("사용자를 찾을 수 없음"));
    }
    // 프로필 조회
    public Object getCompanyInfo(Long companyId) {
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
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
        if (findCompany.isEmpty()) {
            return new CompanyDto.CompanyResponse(ExceptionCode.USER_NOT_FOUND);
        }
        Company company = findCompany.get();
        company.updateCompany(updateCompany);
        //추가
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        company.setPw(encoder.encode(updateCompany.getPw()));
        if(imageFile != null && !imageFile.isEmpty()) {
            String image = fileService.saveFile(company.getCompanyId(), imageFile, "company");
            company.setImagePath(image);
        }else {
            company.setImagePath(null);
        }

        companyRepository.save(company);

        return new CompanyDto.CompanyResponse(ExceptionCode.USER_UPDATE_OK);
    }
}
