package com.example.job_mountain.validation;

import lombok.Getter;

@Getter
public enum HttpStatus {
    SUCCESS(200),
    CREATED(201),
    NOTSAME(202),

    UNAUTHORIZED(401),
    NOT_FOUND_VALUE(404),
    DUPLICATED_VALUE(409),
    INVALID_ACCESS(403);

    public int value;

    HttpStatus(int value) {
        this.value = value;
    }
    
}
