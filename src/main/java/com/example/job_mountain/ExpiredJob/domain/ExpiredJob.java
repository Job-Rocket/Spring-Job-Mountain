package com.example.job_mountain.ExpiredJob.domain;

import com.example.job_mountain.job.domain.Job;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ExpiredJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expiredJobId;
    private String title;
    private String content;
    private LocalDate deadline;
    private LocalDate expiredDate;

    @Builder
    public ExpiredJob(Job job) {
        this.title = job.getTitle();
        this.content = job.getContent();
        this.deadline = job.getDeadline();
        this.expiredDate = LocalDate.now();
    }

}
