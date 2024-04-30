package com.example.job_mountain.job.controller;

import com.example.job_mountain.job.dto.JobDto;
import com.example.job_mountain.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    //글 작성
    @PostMapping("/job/{companyId}")
    public ResponseEntity<Object> savePost(@PathVariable Long companyId,
                                           @RequestBody JobDto.CreateJob createJob) {
        return new ResponseEntity<>(jobService.createPost(companyId, createJob), HttpStatus.OK);
    }


    //글 수정
    @PatchMapping("/job/{companyId}/{jobId}")
    public ResponseEntity<Object> updateJob(@PathVariable Long companyId,
                                            @PathVariable Long jobId,
                                            @RequestBody JobDto.CreateJob createJob) {
        return new ResponseEntity<>(jobService.updateJob(companyId, jobId, createJob), HttpStatus.OK);
    }

    //글 삭제
    @DeleteMapping("/job/{companyId}/{jobId}")
    public ResponseEntity<Object> deletePost(@PathVariable Long companyId,
                                             @PathVariable Long jobId) {
        return new ResponseEntity<>(jobService.deleteJob(companyId, jobId), HttpStatus.OK);
    }

    // 글 전체 불러오기
    @GetMapping("/all/jobs")
    public ResponseEntity<Object> getAllJobs() {
        return new ResponseEntity<>(jobService.findAllJobs(), HttpStatus.OK);
    }

    // 조회수
    @GetMapping("/job/{jobId}")
    public ResponseEntity<Object> getJob(@PathVariable Long jobId) {
        // Job 조회 로직 호출
        Object result = jobService.getJob(jobId);
        jobService.getView(jobId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
