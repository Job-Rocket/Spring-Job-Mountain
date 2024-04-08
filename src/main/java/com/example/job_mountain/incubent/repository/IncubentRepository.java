package com.example.job_mountain.incubent.repository;

import com.example.job_mountain.incubent.domain.Incubent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncubentRepository extends JpaRepository<Incubent, Long> {

}
