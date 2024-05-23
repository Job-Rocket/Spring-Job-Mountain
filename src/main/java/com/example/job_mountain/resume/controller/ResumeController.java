package com.example.job_mountain.resume.controller;

import com.example.job_mountain.resume.dto.ResumeDto;
import com.example.job_mountain.resume.service.ResumeService;
import com.example.job_mountain.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    // shorts 이력서 저장, 삭제 기능은 ShortsController에 있음

    // video 이력서 저장
    @PostMapping("/user/resume/{jobId}")
    public ResponseEntity<Object> createResume(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable Long jobId,
                                               @RequestPart("createResume") ResumeDto.CreateResume createResume,
                                               @RequestPart("file") MultipartFile file) {
        return new ResponseEntity<>(resumeService.createResume(userPrincipal, jobId, createResume, file), HttpStatus.OK);
    }

    // video 이력서 수정
    @PatchMapping("/user/resume/{resumeId}")
    public ResponseEntity<Object> updateResume(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable Long resumeId,
                                               @RequestPart("updateResume") ResumeDto.CreateResume updateResume,
                                               @RequestPart("file") MultipartFile file) {
        return new ResponseEntity<>(resumeService.updateResume(userPrincipal, resumeId, updateResume, file), HttpStatus.OK);
    }

    // video 이력서 삭제
    @DeleteMapping("/user/resume/{resumeId}")
    public ResponseEntity<Object> deleteResume(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable Long resumeId) {
        return new ResponseEntity<>(resumeService.deleteResume(userPrincipal, resumeId), HttpStatus.OK);
    }

    // video 이력서 전체 불러오기
    @GetMapping("/all/resumes")
    public ResponseEntity<Object> getAllResumes() {
        return new ResponseEntity<>(resumeService.findAllResumes(), HttpStatus.OK);
    }

    // video 이력서 조회시, 조회수 증가
    @GetMapping("/all/resume/{resumeId}")
    public ResponseEntity<Object> getResume(@PathVariable Long resumeId) {
        return new ResponseEntity<>(resumeService.getView(resumeId), HttpStatus.OK);
    }

    // video 이력서에서 좋아요 누르면, 좋아요 증가
    @PostMapping("/all/resume/like/{resumeId}")
    public ResponseEntity<Object> addResumenumlikes(@PathVariable Long resumeId) {
        return new ResponseEntity<>(resumeService.addNumLikes(resumeId), HttpStatus.OK);
    }

    // video 이력서 Top5 불러오기
    @GetMapping("/all/5resumes")
    public ResponseEntity<Object> getTop5Resumes() {
        return new ResponseEntity<>(resumeService.getTop5ResumesByNumLikes(), HttpStatus.OK);
    }

}

