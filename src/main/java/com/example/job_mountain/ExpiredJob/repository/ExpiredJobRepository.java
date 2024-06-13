package com.example.job_mountain.ExpiredJob.repository;

import com.example.job_mountain.ExpiredJob.domain.ExpiredJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpiredJobRepository extends JpaRepository<ExpiredJob, Long> {
    List<ExpiredJob> findByDeadlineBefore(LocalDate date);
    Optional<ExpiredJob> findByExpiredJobId(Long jobId);
}
