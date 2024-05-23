package com.example.job_mountain.resume.repository;

import com.example.job_mountain.resume.domain.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findAllByOrderByCreatedAtDesc();
    Optional<Resume> findByResumeId(Long resumeId);
    List<Resume> findTop5ByOrderByNumLikesDesc();
}
