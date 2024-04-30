package com.example.job_mountain.shorts.domain;

import com.example.job_mountain.config.BaseEntity;
import com.example.job_mountain.job.domain.Job;
import com.example.job_mountain.shorts.dto.ShortsDto;
import com.example.job_mountain.user.domain.SiteUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Shorts extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shorts_id")
    private Long shortsId;// 식별을 위한 아이디

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;// 채용공고

    @Column(name = "job_title")
    private String jobTitle;// 채용공고 제목

    // 취준생과 이력서 간의 다대일 관계
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private SiteUser siteUser;

    @Column(name = "user_name")
    private String userName;// 작성자 이름

    @Column(name = "user_image")
    private String userImage;// 작성자 프로필이미지

    private String title;// 이력서 제목
    private String file; // 비디오 파일
    private int view;// 조회수
    private int num_likes;// 좋아요

    // 추가
    @Builder
    public Shorts(Job job, SiteUser user, ShortsDto.CreateShorts createShorts) {
        this.job = job;
        this.jobTitle = job.getTitle();
        this.siteUser = user;
        this.userName = user.getUserName();
        this.userImage = user.getImagePath();
        this.title = createShorts.getTitle();
        this.file = createShorts.getFile();
        this.view = 0;
        this.num_likes = 0;
    }
    public void updateShorts(ShortsDto.CreateShorts createShorts) {
        this.title = createShorts.getTitle();
        this.file = createShorts.getFile();
    }
}
