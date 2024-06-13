package com.example.job_mountain.ExpiredJob.controller;

import com.example.job_mountain.ExpiredJob.service.ExpiredJobService;
import com.example.job_mountain.job.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/all")
public class ExpiredJobController {

    @Autowired
    private ExpiredJobService expiredJobService;

    @Autowired
    private JobService jobService;

    @GetMapping
    public ResponseEntity<Object> getAllExpiredJobs() {
        Object expiredJobs = expiredJobService.getAllExpiredJobs();
        return new ResponseEntity<>(expiredJobs,HttpStatus.OK);
    }

    @GetMapping("/expiredjobs/{id}")
    public ResponseEntity<Object> getExpiredJobById(@PathVariable Long id) {
        Object expiredJob = expiredJobService.getExpiredJobById(id);
        if (expiredJob != null) {
            return new ResponseEntity<>(expiredJob, HttpStatus.OK);
        } else {

            return new ResponseEntity<>(expiredJob, HttpStatus.NOT_FOUND);
        }
    }

}
