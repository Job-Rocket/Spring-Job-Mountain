package com.example.job_mountain.user.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncubentCreateForm {
    @Size(min = 3, max = 25)
    @NotEmpty(message = "이름은 필수항목입니다.")
    private String incuname;

    @NotEmpty(message = "사용자ID는 필수항목입니다.")
    private String incuId;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String pw1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String pw2;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;

    private Integer age;

    private String companyin;
}
