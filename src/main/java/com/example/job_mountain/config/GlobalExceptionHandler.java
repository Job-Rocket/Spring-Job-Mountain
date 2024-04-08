package com.example.job_mountain.config;

import com.example.job_mountain.validation.ExceptionCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Response> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e) {
        Response response = new Response(ExceptionCode.FILE_SIZE_EXCEED);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
    }

    @Getter
    @Setter
    public static class Response {
        private final com.example.job_mountain.validation.HttpStatus status;
        private final String code;
        private final String message;

        public Response(ExceptionCode exceptionCode) {
            this.status = exceptionCode.getStatus();
            this.code = exceptionCode.getCode();
            this.message = exceptionCode.getMessage();
        }
    }
}