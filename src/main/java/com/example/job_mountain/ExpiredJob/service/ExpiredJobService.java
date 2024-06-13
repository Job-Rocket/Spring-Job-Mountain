package com.example.job_mountain.ExpiredJob.service;

import com.example.job_mountain.ExpiredJob.domain.ExpiredJob;
import com.example.job_mountain.ExpiredJob.dto.ExpiredJobDto;
import com.example.job_mountain.ExpiredJob.repository.ExpiredJobRepository;
import com.example.job_mountain.validation.ExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpiredJobService {
    @Autowired
    private ExpiredJobRepository expiredJobRepository;

    public void saveExpiredJob(ExpiredJob expiredJob) {
        expiredJobRepository.save(expiredJob);
    }

    public Object getAllExpiredJobs() {
        List<ExpiredJob> all = expiredJobRepository.findAll();
        return new ExpiredJobDto.getAllExpiredJobs(ExceptionCode.EXPIREDJOB_GET_OK,all);
    }

    public Object getExpiredJobById(Long id) {
        Optional<ExpiredJob> expiredJob = expiredJobRepository.findByExpiredJobId(id);
        if(expiredJob.isPresent()){
            return expiredJob.get();
        }
        else{
            return new ExpiredJobDto.ExpiredJobResponse(ExceptionCode.EXPIREDJOB_GET_FAIL);
        }
    }

}
