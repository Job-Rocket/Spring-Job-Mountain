package com.example.job_mountain.job.domain;

import com.example.job_mountain.company.domain.Company;
import com.example.job_mountain.config.BaseEntity;
import com.example.job_mountain.job.dto.JobDto;
import com.example.job_mountain.resume.domain.Resume;
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
    private String companyLocation;//회사위치

    private String title;// 공고 제목
    private LocalDate deadline;//마감기한
    private String content;//공고 내용
    private int view;//조회수
    // private String RegistNum; //등록일자

    @JsonIgnore
    @OneToMany(mappedBy = "job", cascade = CascadeType.REMOVE) // 이력서(resume) 테이블과 일대다매핑
    private List<Resume> resumes;

    // 추가
    @Builder
    public Job(Company company, JobDto.CreateJob createJob) {
        this.company = company;
        this.companyImage = company.getImagePath();
        this.companyName = company.getCompanyName();
        this.companyLocation = company.getCompanyLocation();
        this.title = createJob.getTitle();
        this.deadline = createJob.getDeadline();
        this.content = createJob.getContent();
        this.view = 0;
    }
    public void updateJob(JobDto.CreateJob createJob) {
        this.title = createJob.getTitle();
        this.deadline = createJob.getDeadline();
        this.content = createJob.getContent();
        this.view = createJob.getView();
    }
}
