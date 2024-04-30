package com.example.job_mountain.security;

import com.example.job_mountain.company.domain.Company;
import com.example.job_mountain.company.repository.CompanyRepository;
import com.example.job_mountain.user.domain.SiteUser;
import com.example.job_mountain.user.repository.UserRepository;
import com.example.job_mountain.validation.CustomException;
import com.example.job_mountain.validation.ExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService { // 로그인된 사용자 처리

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    private static final Integer STATUS = 1;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        System.out.println(email);
        Optional<SiteUser> user = userRepository.findById(email);
        if(user.isEmpty() || user == null) {
            Company companyUser = companyRepository.findById(email).orElseThrow(() -> {
                throw new CustomException(ExceptionCode.USER_NOT_FOUND);
            });
            return CompanyPrincipal.create(companyUser);
        } else {
            return UserPrincipal.create(user.get());
        }
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        System.out.println(id);
        Optional<SiteUser> user = userRepository.findById(id);
        if(user.isEmpty() || user == null) {
            Company companyUser = companyRepository.findById(id).orElseThrow(() -> {
                throw new CustomException(ExceptionCode.USER_NOT_FOUND);
            });
            return CompanyPrincipal.create(companyUser);
        } else {
            return UserPrincipal.create(user.get());
        }
    }
}