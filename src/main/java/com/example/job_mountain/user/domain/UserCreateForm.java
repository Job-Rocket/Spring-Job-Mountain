package com.example.job_mountain.user.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserCreateForm {
    @Size(min = 3, max = 25)
    @NotEmpty(message = "이름은 필수항목입니다.")
    private String userName;

    @NotEmpty(message = "사용자ID는 필수항목입니다.")
    private String id;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String pw;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String pw2;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;

    private String image;

    private Integer age;

    private List<String> interest;
}
