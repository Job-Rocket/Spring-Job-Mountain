package com.example.job_mountain.user.domain;

import com.example.job_mountain.comment.domain.Comment;
import com.example.job_mountain.post.domain.Post;
import com.example.job_mountain.resume.domain.Resume;
import com.example.job_mountain.shorts.domain.Shorts;
import com.example.job_mountain.user.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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

    // 취준생과 이력서 간의 일대다 관계
    @JsonIgnore
    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.ALL)
    private List<Resume> resumes = new ArrayList<>();

    // 취준생과 이력서 간의 일대다 관계
    @JsonIgnore
    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.ALL)
    private List<Shorts> shorts = new ArrayList<>();

    // 유저와 Post 간의 일대다 관계
    @JsonIgnore
    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    // 유저와 Comment 간의 일대다 관계
    @JsonIgnore
    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();


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
        //this.id = updateUser.getId();
        this.pw = updateUser.getPw();
        //this.email = updateUser.getEmail();
        this.age = updateUser.getAge();
        this.imagePath = updateUser.getImagePath(); // 추가
        this.interest = updateUser.getInterest();
        // this.image = updateUser.getImage();
    }

}
