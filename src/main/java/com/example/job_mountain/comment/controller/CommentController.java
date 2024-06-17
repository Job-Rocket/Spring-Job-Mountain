package com.example.job_mountain.comment.controller;

import com.example.job_mountain.comment.dto.CommentDto;
import com.example.job_mountain.comment.service.CommentService;
import com.example.job_mountain.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 달기 및 댓글 전체 불러오기
    @PostMapping("/user/comment/{postId}")
    public ResponseEntity<Object> createComment(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @PathVariable Long postId,
                                                @RequestBody CommentDto.CreateComment createComment) {
        commentService.createComment(userPrincipal, postId, createComment);
        return new ResponseEntity<>(commentService.findCommentsByPostId(postId), HttpStatus.OK);
    }

    // 댓글 수정하기 및 댓글 전체 불러오기
    @PatchMapping("/user/comment/{postId}/{commentId}")
    public ResponseEntity<Object> updateComment(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @PathVariable Long postId,
                                                @PathVariable Long commentId,
                                                @RequestBody CommentDto.CreateComment updateComment) {
        commentService.updateComment(userPrincipal, postId, commentId, updateComment);
        return new ResponseEntity<>(commentService.findCommentsByPostId(postId), HttpStatus.OK);
    }

    // 댓글 삭제하기 및 댓글 전체 불러오기
    @DeleteMapping("/user/comment/{postId}/{commentId}")
    public ResponseEntity<Object> deleteComment(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @PathVariable Long postId,
                                                @PathVariable Long commentId) {
        commentService.deleteComment(userPrincipal, postId, commentId);
        return new ResponseEntity<>(commentService.findCommentsByPostId(postId), HttpStatus.OK);
    }

    // 특정 게시물 댓글 불러오기
    @GetMapping("/all/post/comments/{postId}")
    public ResponseEntity<Object> getPostsComment(@PathVariable Long postId) {
        return new ResponseEntity<>(commentService.findCommentsByPostId(postId), HttpStatus.OK);
    }

    // comment 좋아요 누르면, 좋아요 증가
    @PostMapping("/all/comment/like/{commentId}")
    public ResponseEntity<Object> addCommentnumlikes(@PathVariable Long commentId) {
        return new ResponseEntity<>(commentService.addNumLikes(commentId), HttpStatus.OK);
    }

}
