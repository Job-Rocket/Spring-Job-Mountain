package com.example.job_mountain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenDto {

    private String accessToken;
    private String refreshToken;
}
