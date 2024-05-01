package com.example.job_mountain.job.service;

import com.example.job_mountain.company.domain.Company;
import com.example.job_mountain.company.repository.CompanyRepository;
import com.example.job_mountain.job.domain.Job;
import com.example.job_mountain.job.dto.JobDto;
import com.example.job_mountain.job.repository.JobRepository;
import com.example.job_mountain.validation.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;

    // 채용공고 저장
    public Object createPost(Long companyId, JobDto.CreateJob createJob) {
        Company company = companyRepository.findByCompanyId(companyId).get();
        System.out.println("j01");

        // 제목, 설명 둘다 있어야 채용공고를 저장함
        if (!createJob.getTitle().isEmpty() && !createJob.getContent().isEmpty()) {
            Job job = Job.builder()
                    .createJob(createJob)
                    .company(company)
                    .build();

            // Job 저장 후 반환
            jobRepository.save(job);
            return new JobDto.JobResponse(ExceptionCode.JOB_SAVE_OK);
        } else if (createJob.getTitle().isEmpty()) { // 제목 없을 때 반환하는 로직
            return new JobDto.JobResponse(ExceptionCode.JOB_SAVE_FAIL1);
        } else { // 제목은 있으나 내용이 없을 때 반환하는 로직
            return new JobDto.JobResponse(ExceptionCode.JOB_SAVE_FAIL2);
        }
    }
    // 채용공고 수정
    public Object updateJob(Long companyId, Long jobId, JobDto.CreateJob createJob) {
        Company company = companyRepository.findByCompanyId(companyId).get();

        Optional<Job> findJob = jobRepository.findByJobId(jobId);
        if (findJob.isEmpty()) {
            return new JobDto.JobResponse(ExceptionCode.JOB_NOT_FOUND); // 수정
        }
        Job job = findJob.get();

        if (!job.getCompany().equals(company)) {
            return new JobDto.JobResponse(ExceptionCode.INVALID_USER); // 수정
        }

        // 제목, 설명 둘다 있어야 채용공고를 저장함
        if (!createJob.getTitle().isEmpty() && !createJob.getContent().isEmpty()) {
            job.updateJob(createJob);
            jobRepository.save(job);
            return new JobDto.JobResponse(ExceptionCode.JOB_UPDATE_OK);
        } else if (createJob.getTitle().isEmpty()) { // 제목 없을 때 반환하는 로직
            return new JobDto.JobResponse(ExceptionCode.JOB_SAVE_FAIL1);
        } else { // 제목은 있으나 내용이 없을 때 반환하는 로직
            return new JobDto.JobResponse(ExceptionCode.JOB_SAVE_FAIL2);
        }
    }
    // 채용공고 삭제
    public Object deleteJob(Long companyId, Long jobId) {
        Company company = companyRepository.findByCompanyId(companyId).get();

        Optional<Job> findJob = jobRepository.findByJobId(jobId);
        if (findJob.isEmpty()) {
            return new JobDto.JobResponse(ExceptionCode.JOB_NOT_FOUND);
        }
        Job job = findJob.get();

        if (!job.getCompany().equals(company)) {
            return new JobDto.JobResponse(ExceptionCode.INVALID_USER);
        }

        jobRepository.delete(job);
        return new JobDto.JobResponse(ExceptionCode.JOB_DELETE_OK);
    }

    public Object findAllJobs() {
        List<Job> all = jobRepository.findAllByOrderByCreatedAtDesc();
        return new JobDto.JobListResponse(ExceptionCode.JOB_GET_OK, all);
    }

    // 채용공고 호출
    public Object getJob(Long jobId) {
        Optional<Job> optionalJob = jobRepository.findByJobId(jobId);
        if (optionalJob.isPresent()) {
            return optionalJob.get();
        } else {
            throw new IllegalArgumentException("Job not found with jobId: " + jobId);
        }
    }

    // 조회수
    public Object getView(Long jobId) {
        Optional<Job> optionalJob = jobRepository.findByJobId(jobId);
        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();
            job.setView(job.getView() + 1);
            this.jobRepository.save(job);
            return job;
        } else {
            throw new IllegalArgumentException("Job not found");
        }
    }
}
