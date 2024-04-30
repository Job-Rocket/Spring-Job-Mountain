package com.example.job_mountain.job.dto;

import com.example.job_mountain.config.ResponseType;
import com.example.job_mountain.job.domain.Job;
import com.example.job_mountain.validation.ExceptionCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class JobDto {

    // 채용공고 올리기, 수정하기
    @Getter
    @Setter
    public static class CreateJob {
        private String title;
        private LocalDate deadline;
        private String content;
        private int view;
    }

    @Getter
    public static class JobResponse extends ResponseType {

        @JsonInclude(NON_NULL)
        private Job job;

        public JobResponse(ExceptionCode exceptionCode) {
            super(exceptionCode);
        }
        public JobResponse(ExceptionCode exceptionCode, Job job) {
            super(exceptionCode);
            this.job = job;
        }
    }

    @Getter
    public static class JobListResponse extends ResponseType{
        List<Job> jobList;
        public JobListResponse(ExceptionCode exceptionCode, List<Job> jobList) {
            super(exceptionCode);
            this.jobList = jobList;
        }
    }

}
