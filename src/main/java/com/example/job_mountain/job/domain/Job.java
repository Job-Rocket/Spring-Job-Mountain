package com.example.job_mountain.job.domain;

import com.example.job_mountain.company.domain.Company;
import com.example.job_mountain.config.BaseEntity;
import com.example.job_mountain.job.dto.JobDto;
import com.example.job_mountain.resume.domain.Resume;
import com.example.job_mountain.shorts.domain.Shorts;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Job extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId;//식별을 위한 아이디

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;// 회사정보

    @Column(name = "company_image")
    private String companyImage;// 회사이미지

    @Column(name = "company_name")
    private String companyName;// 회사이름

    @Column(name = "company_location")
    private String companyLocation;// 회사위치

    @Column(name = "company_employees")
    private Integer companyEmployees;// 회사직원수

    @Column(name = "company_stack")
    private List<String> companyStack;// 회사기술스택

    private String title;// 공고 제목
    private LocalDate deadline;//마감기한
    private String content;//공고 내용

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> env; // 사용 환경

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> worktype; // 근무형태

    private Integer salary; // 해당채용공고의 연봉

    private int view;//조회수


    // 이력서(resume) 테이블과 일대다매핑
    @JsonIgnore
    @OneToMany(mappedBy = "job", cascade = CascadeType.REMOVE)
    private List<Resume> resumes;

    // 쇼츠(shorts) 테이블과 일대다매핑
    @JsonIgnore
    @OneToMany(mappedBy = "job", cascade = CascadeType.REMOVE)
    private List<Shorts> shorts;


    // 추가
    @Builder
    public Job(Company company, JobDto.CreateJob createJob) {
        this.company = company;
        this.companyImage = company.getImagePath();
        this.companyName = company.getCompanyName();
        this.companyLocation = company.getCompanyLocation();
        this.companyEmployees = company.getEmployees();
        this.companyStack = company.getStack();
        this.title = createJob.getTitle();
        this.deadline = createJob.getDeadline();
        this.content = createJob.getContent();
        this.env = createJob.getEnv();
        this.worktype = createJob.getWorktype();
        this.salary = createJob.getSalary();
        this.view = 0;
    }
    public void updateJob(JobDto.CreateJob createJob) {
        this.title = createJob.getTitle();
        this.deadline = createJob.getDeadline();
        this.content = createJob.getContent();
        this.env = createJob.getEnv();
        this.worktype = createJob.getWorktype();
        this.salary = createJob.getSalary();
        this.view = createJob.getView();
    }
}
