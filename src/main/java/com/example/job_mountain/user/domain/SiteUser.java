package com.example.job_mountain.user.domain;

import com.example.job_mountain.user.dto.UserDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String pw;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

//    @Column(nullable = true)
//    private String image;

    @Column(nullable = true) // 추가
    private String imagePath;

    @Column()
    private Integer age;

    @ElementCollection(fetch = FetchType.LAZY) // 관심분야를 위한 수정
    private List<String> interest;

    @Column(length = 400)
    private String token;

    // builder
    @Builder
    public SiteUser(UserDto.SignupUser signupUser, String pw) {
        this.id = signupUser.getId();
        this.pw = pw;
        this.userName = signupUser.getUserName();
        this.email = signupUser.getEmail();
        this.age = signupUser.getAge();
        this.interest = signupUser.getInterest();
        // this.image = signupUser.getImage();
        this.imagePath = signupUser.getImagePath();
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void updateUser(UserDto.UpdateUser updateUser) {
        this.id = updateUser.getId();
        this.pw = updateUser.getPw();
        this.age = updateUser.getAge();
        this.interest = updateUser.getInterest();
        // this.image = updateUser.getImage();
        this.imagePath = updateUser.getImagePath(); // 추가
    }

}
