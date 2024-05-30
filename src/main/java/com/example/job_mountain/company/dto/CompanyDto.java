package com.example.job_mountain.company.dto;

import com.example.job_mountain.company.domain.Company;
import com.example.job_mountain.config.ResponseType;
import com.example.job_mountain.user.dto.TokenDto;
import com.example.job_mountain.validation.ExceptionCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class CompanyDto {
    // 회원가입
    @Getter
    public static class SignupCompany{
        String id;
        String pw;
        String companyName;
        String companyNo;
        String imagePath;
        Integer averageSalary;
        Integer employees;
        String companyLocation;
        List<String> stack;
    }

    @Getter
    public static class TokenResponse extends ResponseType {
        TokenDto tokenDto;

        public TokenResponse(ExceptionCode exceptionCode, TokenDto tokenDto){
            super(exceptionCode);
            this.tokenDto = tokenDto;
        }
    }
    @Getter
    public static class DuplicateCompanyResponse extends ResponseType { // 회원가입시 ID, Email 중복, 로그인시 ID, PW 오류를 반환하기 위함
        public DuplicateCompanyResponse(ExceptionCode exceptionCode) {
            super(exceptionCode);
        }
    }

    @Getter
    public static class CompanyResponse extends ResponseType{

        @JsonInclude(NON_NULL)
        Long companyId;

        @JsonInclude(NON_NULL)
        List<Company> companys;

        public CompanyResponse(ExceptionCode exceptionCode) {
            super(exceptionCode);
        }

        public CompanyResponse(ExceptionCode exceptionCode, Long companyId) {
            super(exceptionCode);
            this.companyId = companyId;
        }

        public CompanyResponse(ExceptionCode exceptionCode, List<Company> companys) {
            super(exceptionCode);
            this.companys = companys;
        }
    }
    // 로그인
    @Getter
    public static class LoginCompany{
        String id;
        String pw;
    }
    @Getter
    public static class LoginResponse extends ResponseType{
        Long companyId;
        String companyName;
        TokenDto tokenDto;
        public LoginResponse(ExceptionCode exceptionCode, Company company, TokenDto tokenDto) {
            super(exceptionCode);
            this.companyId = company.getCompanyId();
            this.companyName = company.getCompanyName();
            this.tokenDto = tokenDto;
        }
    }
    // 로그아웃
    @Getter
    public static class LogoutCompany {
        Long companyId;
        String accessToken;
    }
    @Getter
    public static class LogoutResponse extends ResponseType {
        public LogoutResponse(ExceptionCode exceptionCode) {
            super(exceptionCode);
        }
    }
    // 정보 조회
    @Getter
    public static class CompanyInfoResponse extends ResponseType{
        Long companyId;
        String companyName;
        String id;
        String pw;
        String companyNo;
        String imagePath;
        Integer averageSalary;
        Integer employees;
        String companyLocation;
        List<String> stack;

        public CompanyInfoResponse(ExceptionCode exceptionCode) {
            super(exceptionCode);
        }

        public CompanyInfoResponse(ExceptionCode exceptionCode, Company company){
            super(exceptionCode);
            this.companyId = company.getCompanyId();
            this.companyName = company.getCompanyName();
            this.id = company.getId();
            this.pw = company.getPw();
            this.companyNo = company.getCompanyNo();
            this.imagePath = company.getImagePath();
            this.averageSalary = company.getAverageSalary();
            this.employees = company.getEmployees();
            this.companyLocation = company.getCompanyLocation();
            this.stack = company.getStack();
        }
    }
    // 정보 수정
    @Getter
    public static class UpdateCompany {
        //String id;
        String pw;
        String companyName;
        // String image
        String companyNo;
        String imagePath; // 추가
        Integer averageSalary;
        Integer employees;
        String companyLocation;
        List<String> stack;
    }

}
