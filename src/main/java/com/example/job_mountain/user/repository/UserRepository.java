package com.example.job_mountain.user.repository;

import com.example.job_mountain.user.domain.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findById(String id);
    Optional<SiteUser> findByUserId(Long userId);
    Optional<SiteUser> findByEmail(String email);

    Optional<SiteUser> findByToken(String token);
    // @Cacheable(cacheNames = CacheNames.USERBYUSERID, key = "'login'+#p0", unless = "#result==null")

    boolean existsById(String id);
}