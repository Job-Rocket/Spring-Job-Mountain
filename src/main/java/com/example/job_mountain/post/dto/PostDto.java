package com.example.job_mountain.post.dto;

import com.example.job_mountain.config.ResponseType;
import com.example.job_mountain.post.domain.Post;
import com.example.job_mountain.validation.ExceptionCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class PostDto {
    @Getter
    @Setter
    public static class CreatePost {
        private String title;
        private String content;
    }
    @Getter
    public static class PostResponse extends ResponseType {

        @JsonInclude(NON_NULL)
        private Post post;

        public PostResponse(ExceptionCode exceptionCode) {
            super(exceptionCode);
        }
        public PostResponse(ExceptionCode exceptionCode, Post post) {
            super(exceptionCode);
            this.post = post;
        }
    }

    @Getter
    public static class PostListResponse extends ResponseType{
        List<Post> postList;
        public PostListResponse(ExceptionCode exceptionCode, List<Post> postList) {
            super(exceptionCode);
            this.postList = postList;
        }
    }
}
