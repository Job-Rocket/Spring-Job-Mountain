package com.example.job_mountain.comment.dto;

import com.example.job_mountain.comment.domain.Comment;
import com.example.job_mountain.config.ResponseType;
import com.example.job_mountain.validation.ExceptionCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class CommentDto {

    @Getter
    @Setter
    public static class CreateComment {
        private String content;
    }

    @Getter
    public static class CommentResponse extends ResponseType {
        @JsonInclude(NON_NULL)
        private Comment comment;

        public CommentResponse(ExceptionCode exceptionCode) {
            super(exceptionCode);
        }

        public CommentResponse(ExceptionCode exceptionCode, Comment comment) {
            super(exceptionCode);
            this.comment = comment;
        }
    }

    @Getter
    public static class CommentListResponse extends ResponseType {
        @JsonInclude(NON_NULL)
        private List<Comment> commentList;

        public CommentListResponse(ExceptionCode exceptionCode) {
            super(exceptionCode);
        }

        public CommentListResponse(ExceptionCode exceptionCode, List<Comment> commentList) {
            super(exceptionCode);
            this.commentList = commentList;
        }
    }
}
