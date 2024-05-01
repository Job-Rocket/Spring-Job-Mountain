package com.example.job_mountain.shorts.controller;

import com.example.job_mountain.security.UserPrincipal;
import com.example.job_mountain.shorts.dto.ShortsDto;
import com.example.job_mountain.shorts.service.ShortsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ShortsController {

    private final ShortsService shortsService;

    // shorts 이력서 저장
    @PostMapping("/user/shorts/{jobId}")
    public ResponseEntity<Object> createShorts(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable Long jobId,
                                               @RequestPart("createShorts") ShortsDto.CreateShorts createShorts,
                                               @RequestPart("file") MultipartFile file) {
        return new ResponseEntity<>(shortsService.createShorts(userPrincipal, jobId, createShorts, file), HttpStatus.OK);
    }

    // shorts 이력서 수정
    @PatchMapping("/user/shorts/{shortsId}")
    public ResponseEntity<Object> updateShorts(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable Long shortsId,
                                               @RequestPart("updateShorts") ShortsDto.CreateShorts updateShorts,
                                               @RequestPart("file") MultipartFile file) {
        return new ResponseEntity<>(shortsService.updateShorts(userPrincipal, shortsId, updateShorts, file), HttpStatus.OK);
    }

    // shorts 이력서 삭제
    @DeleteMapping("/user/shorts/{shortsId}")
    public ResponseEntity<Object> deleteShorts(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable Long shortsId) {
        return new ResponseEntity<>(shortsService.deleteShorts(userPrincipal, shortsId), HttpStatus.OK);
    }

    // shorts 이력서 전체 불러오기
    @GetMapping("/all/shorts")
    public ResponseEntity<Object> getAllShorts() {
        return new ResponseEntity<>(shortsService.findAllShorts(), HttpStatus.OK);
    }

    // video 이력서 조회시, 조회수 증가
    @GetMapping("/all/shorts/{shortsId}")
    public ResponseEntity<Object> getShorts(@PathVariable Long shortsId) {
        return new ResponseEntity<>(shortsService.getView(shortsId), HttpStatus.OK);
    }

    // video 이력서에서 좋아요 누르면, 좋아요 증가
    @PostMapping("/all/shorts/like/{shortsId}")
    public ResponseEntity<Object> addShortsnumlikes(@PathVariable Long shortsId) {
        return new ResponseEntity<>(shortsService.addNumLikes(shortsId), HttpStatus.OK);
    }
}

