package com.example.job_mountain.comment.domain;

import com.example.job_mountain.comment.dto.CommentDto;
import com.example.job_mountain.config.BaseEntity;
import com.example.job_mountain.post.domain.Post;
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
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private SiteUser siteUser;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_image")
    private String userImage;

    @Column(name ="user_flag")
    private boolean userFlag; // True(1)면 취준생, False(0)면 현직자

    private String content;
    private int numLikes;

    @Builder
    public Comment(SiteUser user, Post post, CommentDto.CreateComment createComment) {
        this.siteUser = user;
        this.userName = user.getUserName();
        this.userImage = user.getImagePath();
        this.userFlag = user.getFlag();
        this.content = createComment.getContent();
        this.numLikes = 0;
        this.post = post;
    }
    public void updateComment(CommentDto.CreateComment createComment) {
        this.content = createComment.getContent();
    }
}
