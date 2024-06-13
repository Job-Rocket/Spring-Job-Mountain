package com.example.job_mountain.post.controller;

import com.example.job_mountain.post.dto.PostDto;
import com.example.job_mountain.post.service.PostService;
import com.example.job_mountain.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // post 저장
    @PostMapping("/user/post")
    public ResponseEntity<Object> createPost(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @RequestPart("createPost") PostDto.CreatePost createPost) {
        return new ResponseEntity<>(postService.createPost(userPrincipal, createPost), HttpStatus.OK);
    }

    // post 수정
    @PatchMapping("/user/post/{postId}")
    public ResponseEntity<Object> updatePost(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable Long postId,
                                               @RequestPart("updatePost") PostDto.CreatePost updatePost) {
        return new ResponseEntity<>(postService.updatePost(userPrincipal, postId, updatePost), HttpStatus.OK);
    }

    // post 삭제
    @DeleteMapping("/user/post/{postId}")
    public ResponseEntity<Object> deletePost(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable Long postId) {
        return new ResponseEntity<>(postService.deletePost(userPrincipal, postId), HttpStatus.OK);
    }

    // post 전체 불러오기
    @GetMapping("/all/posts")
    public ResponseEntity<Object> getAllPosts() {
        return new ResponseEntity<>(postService.findAllPosts(), HttpStatus.OK);
    }

    // post 조회시, 조회수 증가
    @GetMapping("/all/post/{postId}")
    public ResponseEntity<Object> getResume(@PathVariable Long postId) {
        return new ResponseEntity<>(postService.getView(postId), HttpStatus.OK);
    }

    // post 좋아요 누르면, 좋아요 증가
    @PostMapping("/all/post/like/{postId}")
    public ResponseEntity<Object> addPostnumlikes(@PathVariable Long postId) {
        return new ResponseEntity<>(postService.addNumLikes(postId), HttpStatus.OK);
    }

    // post Top6 불러오기
    @GetMapping("/all/6posts")
    public ResponseEntity<Object> getTop6Posts() {
        return new ResponseEntity<>(postService.getTop6PostsByNumLikes(), HttpStatus.OK);
    }

}
