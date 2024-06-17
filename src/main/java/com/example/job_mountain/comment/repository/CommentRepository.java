package com.example.job_mountain.comment.repository;

import com.example.job_mountain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByOrderByCreatedAtDesc();
    List<Comment> findByPost_PostIdOrderByCreatedAtDesc(Long postId);
    Optional<Comment> findByCommentId(Long commentId);
}
