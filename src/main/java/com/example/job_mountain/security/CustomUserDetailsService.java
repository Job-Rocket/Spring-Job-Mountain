package com.example.job_mountain.security;

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

@Service
public class CustomUserDetailsService implements UserDetailsService { // 로그인된 사용자 처리

    @Autowired
    private UserRepository userRepository;
    private static final Integer STATUS = 1;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        SiteUser user = userRepository.findById(email)
                .orElseThrow(() -> {
                    throw new CustomException(ExceptionCode.USER_NOT_FOUND);
                });

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        SiteUser user = userRepository.findByUserId(id)
                .orElseThrow(() -> {
                    throw new CustomException(ExceptionCode.USER_NOT_FOUND);
                });

        return UserPrincipal.create(user);
    }
}