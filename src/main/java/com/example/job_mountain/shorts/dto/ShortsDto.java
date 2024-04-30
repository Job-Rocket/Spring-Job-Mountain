package com.example.job_mountain.shorts.dto;

import com.example.job_mountain.config.ResponseType;
import com.example.job_mountain.shorts.domain.Shorts;
import com.example.job_mountain.validation.ExceptionCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@NoArgsConstructor
public class ShortsDto {

    // 이력서 올리기
    @Getter
    @Setter
    public static class CreateShorts {
        private String title;
        private String file;
    }

    @Getter
    public static class ShortsResponse extends ResponseType {

        @JsonInclude(NON_NULL)
        private Shorts shorts;

        public ShortsResponse(ExceptionCode exceptionCode) {
            super(exceptionCode);
        }
        public ShortsResponse(ExceptionCode exceptionCode, Shorts shorts) {
            super(exceptionCode);
            this.shorts = shorts;
        }
    }

    @Getter
    public static class ShortsListResponse extends ResponseType{
        List<Shorts> shortsList;
        public ShortsListResponse(ExceptionCode exceptionCode, List<Shorts> shortsList) {
            super(exceptionCode);
            this.shortsList = shortsList;
        }
    }


}
