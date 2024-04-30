package com.example.job_mountain.resume.dto;

import com.example.job_mountain.config.ResponseType;
import com.example.job_mountain.resume.domain.Resume;
import com.example.job_mountain.validation.ExceptionCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@NoArgsConstructor
public class ResumeDto {

    // 이력서 올리기
    @Getter
    @Setter
    public static class CreateResume {
        private String title;
        private String file;
    }

    @Getter
    public static class ResumeResponse extends ResponseType {

        @JsonInclude(NON_NULL)
        private Resume resume;

        public ResumeResponse(ExceptionCode exceptionCode) {
            super(exceptionCode);
        }
        public ResumeResponse(ExceptionCode exceptionCode, Resume resume) {
            super(exceptionCode);
            this.resume = resume;
        }
    }

    @Getter
    public static class ResumeListResponse extends ResponseType{
        List<Resume> resumeList;
        public ResumeListResponse(ExceptionCode exceptionCode, List<Resume> resumeList) {
            super(exceptionCode);
            this.resumeList = resumeList;
        }
    }


}
