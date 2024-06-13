package com.example.job_mountain.job.controller;

import com.example.job_mountain.ExpiredJob.service.ExpiredJobService;
import com.example.job_mountain.job.domain.Job;
import com.example.job_mountain.job.dto.JobDto;
import com.example.job_mountain.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;
    private final ExpiredJobService expiredJobService;

    //글 작성
    @PostMapping("/company/job/{companyId}")
    public ResponseEntity<Object> savePost(@PathVariable Long companyId,
                                           @RequestBody JobDto.CreateJob createJob) {
        return new ResponseEntity<>(jobService.createPost(companyId, createJob), HttpStatus.OK);
    }


    //글 수정
    @PatchMapping("/company/job/{companyId}/{jobId}")
    public ResponseEntity<Object> updateJob(@PathVariable Long companyId,
                                            @PathVariable Long jobId,
                                            @RequestBody JobDto.CreateJob createJob) {
        return new ResponseEntity<>(jobService.updateJob(companyId, jobId, createJob), HttpStatus.OK);
    }

    //글 삭제
    @DeleteMapping("/company/job/{companyId}/{jobId}")
    public ResponseEntity<Object> deletePost(@PathVariable Long companyId,
                                             @PathVariable Long jobId) {
        return new ResponseEntity<>(jobService.deleteJob(companyId, jobId), HttpStatus.OK);
    }

    // 글 전체 불러오기
    @GetMapping("/all/jobs")
    public ResponseEntity<Object> getAllJobs() {
        return new ResponseEntity<>(jobService.findAllJobs(), HttpStatus.OK);
    }

    // 채용공고 조회시-조회수 증가
    @GetMapping("/all/job/{jobId}")
    public ResponseEntity<Object> getJob(@PathVariable Long jobId) {
        return new ResponseEntity<>(jobService.getView(jobId), HttpStatus.OK);
    }
    //top6
    @GetMapping("/company/job/top-views")
    public ResponseEntity<List<Job>> findTop6ByOrderByViewDesc(){
        List<Job> topJobs = jobService.getTop6JobsByViews();
        return ResponseEntity.ok(topJobs);
    }
    @GetMapping("/all/checkExpired")
    public ResponseEntity<Object> checkExpiredJobs() {
        jobService.moveExpiredJobs();
        Object expiredJobs = expiredJobService.getAllExpiredJobs();
        return new ResponseEntity<>(expiredJobs,HttpStatus.OK);
    }
    // 메인화면-top6 채용공고
    @GetMapping("/all/job/topviews")
    public ResponseEntity<Object> getTop6JobsByView(){
        return new ResponseEntity<>(jobService.getTop6JobsByViews(), HttpStatus.OK);
    }

}
