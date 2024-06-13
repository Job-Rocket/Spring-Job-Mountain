package com.example.job_mountain.ExpiredJob.dto;

import com.example.job_mountain.ExpiredJob.domain.ExpiredJob;
import com.example.job_mountain.config.ResponseType;
import com.example.job_mountain.validation.ExceptionCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class ExpiredJobDto {
    @Getter
    @Setter
    public static class CreateJob {
        private String title;
        private LocalDate deadline;
        private String content;
        private int view;
    }

    @Getter
    public static class ExpiredJobResponse extends ResponseType {

        @JsonInclude(NON_NULL)
        private ExpiredJob expiredjob;

        public ExpiredJobResponse(ExceptionCode exceptionCode) {
            super(exceptionCode);
        }
        public ExpiredJobResponse(ExceptionCode exceptionCode, ExpiredJob expiredjob) {
            super(exceptionCode);
            this.expiredjob = expiredjob;
        }

    }

    @Getter
    public static class getAllExpiredJobs extends ResponseType{
        List<ExpiredJob> expiredjobList;
        public getAllExpiredJobs(ExceptionCode exceptionCode, List<ExpiredJob> expiredjobList) {
            super(exceptionCode);
            this.expiredjobList = expiredjobList;
        }
    }
}
