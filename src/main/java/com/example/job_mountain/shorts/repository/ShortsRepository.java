package com.example.job_mountain.shorts.repository;

import com.example.job_mountain.shorts.domain.Shorts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShortsRepository extends JpaRepository<Shorts, Long> {

    List<Shorts> findAllByOrderByCreatedAtDesc();
    Optional<Shorts> findByShortsId(Long shortsId);
}
