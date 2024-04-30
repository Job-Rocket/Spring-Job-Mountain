package com.example.job_mountain.post.domain;

import com.example.job_mountain.comment.domain.Comment;
import com.example.job_mountain.config.BaseEntity;
import com.example.job_mountain.user.domain.SiteUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private SiteUser siteUser;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_image")
    private String userImage;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    private String title;
    private String content;
    private int view;
    private int num_likes;
}
