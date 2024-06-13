package com.example.job_mountain.job.repository;

import com.example.job_mountain.job.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findAllByOrderByCreatedAtDesc();
    Optional<Job> findByJobId(Long jobId);

    List<Job> findTop6ByOrderByViewDesc();

    List<Job> findByDeadlineBefore(LocalDate today);
}
