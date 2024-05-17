package com.example.job_mountain.company.controller;

import com.example.job_mountain.company.dto.CompanyDto;
import com.example.job_mountain.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestPart("signupCompany") CompanyDto.SignupCompany signupCompany,
                                         @RequestParam("imageFile") MultipartFile imageFile) {
        Object signupResult = companyService.signup(signupCompany, imageFile);
        return new ResponseEntity<>(signupResult, HttpStatus.OK);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody CompanyDto.LoginCompany loginCompany) {
        return new ResponseEntity<>(companyService.login(loginCompany), HttpStatus.OK);
    }

    // 로그아웃
//    @DeleteMapping("/logout")
//    public ResponseEntity<Object> logout(@RequestBody CompanyDto.LogoutCompany logoutCompany) {
//        return new ResponseEntity<>(companyService.logout(logoutCompany), HttpStatus.OK);
//    }

    // 로그인 후, 회원 정보 조회
    @GetMapping("/{companyId}")
    public ResponseEntity<Object> getCompanyInfo(@PathVariable Long companyId) {
        Object response = companyService.getCompanyInfo(companyId);

        if (response instanceof CompanyDto.CompanyInfoResponse) {
            CompanyDto.CompanyInfoResponse companyInfoResponse = (CompanyDto.CompanyInfoResponse) response;
            if (companyInfoResponse.getCompanyId() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(companyInfoResponse);
            } else {
                return ResponseEntity.ok(companyInfoResponse);
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 로그인 후, 프로필 수정
    @PatchMapping("/{companyId}")
    public ResponseEntity<Object> fixCompanyInfo(@PathVariable Long companyId,
                                                 @RequestPart("updateCompany") CompanyDto.UpdateCompany updateCompany,
                                                 @RequestParam("imageFile") MultipartFile imageFile) {
        Object updateResult = companyService.updateCompany(companyId, updateCompany, imageFile);
        return new ResponseEntity<>(updateResult, HttpStatus.OK);
    }
}
