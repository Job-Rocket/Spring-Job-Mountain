package com.example.job_mountain.post.repository;

import com.example.job_mountain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDesc();
    Optional<Post> findByPostId(Long postId);
    List<Post> findTop6ByOrderByNumLikesDesc();
}
