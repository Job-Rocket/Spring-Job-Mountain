package com.example.job_mountain.company.repository;

import com.example.job_mountain.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findById(String id);
    Optional<Company> findByCompanyId(Long companyId);

    Optional<Company> findByToken(String token);
    // @Cacheable(cacheNames = CacheNames.USERBYUSERID, key = "'login'+#p0", unless = "#result==null")

    boolean existsById(String id);
}