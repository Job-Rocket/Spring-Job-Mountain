package com.example.job_mountain.company.domain;

import com.example.job_mountain.company.dto.CompanyDto;
import com.example.job_mountain.job.domain.Job;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long companyId; // 식별을 위한 아이디

    @Column(nullable = false)
    private String companyName; // 기업 이름

    @Column(nullable = false)
    private String id; // 가입 아이디

    @Column(nullable = false)
    private String pw; // 가입 비밀번호

    @Column(nullable = false)
    private String companyNo; //사업자번호

    @Column(nullable = true) // 추가
    private String imagePath;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> stack; // 기술 스택

    @Column(nullable = true)
    private Integer averageSalary; // 평균 연봉

    @Column(nullable = true)
    private Integer employees; // 직원수

    @Column(nullable = true)
    private String companyLocation; // 회사위치

    @Column(length = 400)
    private String token;

    // 채용공고테이블(job)과 일대다매핑
    @JsonIgnore
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Job> jobs = new ArrayList<>();

    @Builder
    public Company(CompanyDto.SignupCompany signupCompany , String pw){
        this.id = signupCompany.getId();
        this.pw = pw;
        this.companyName = signupCompany.getCompanyName();
        this.companyNo = signupCompany.getCompanyNo();
        this.imagePath = signupCompany.getImagePath();
        this.averageSalary = signupCompany.getAverageSalary();
        this.employees = signupCompany.getEmployees();
        this.companyLocation = signupCompany.getCompanyLocation();
        this.stack = signupCompany.getStack();
    }
    public void setToken(String token) {
        this.token = token;
    }

    public void updateCompany(CompanyDto.UpdateCompany updateCompany) {
        //this.id = updateCompany.getId();
        this.pw = updateCompany.getPw();
        this.companyName= updateCompany.getCompanyName();
        this.companyNo = updateCompany.getCompanyNo();
        this.imagePath = updateCompany.getImagePath(); // 추가
        this.averageSalary = updateCompany.getAverageSalary();
        this.employees = updateCompany.getEmployees();
        this.companyLocation = updateCompany.getCompanyLocation();
        this.stack = updateCompany.getStack();
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                ", id='" + id + '\'' +
                ", pw='" + pw + '\'' +
                ", companyNo='" + companyNo + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", stack=" + stack +
                ", averageSalary=" + averageSalary +
                ", employees=" + employees +
                ", companyLocation='" + companyLocation + '\'' +
                ", token='" + token + '\'' +
                ", jobs=" + jobs +
                '}';
    }
}
