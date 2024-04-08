package com.example.job_mountain.incubent.service;

import com.example.job_mountain.incubent.domain.Incubent;
import com.example.job_mountain.incubent.repository.IncubentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IncubentService {

    private final IncubentRepository incubentRepository;
    private final PasswordEncoder passwordEncoder;

    public Incubent create(String incuname, String incuId, String pw, String email, Integer age, String companyin) {
        Incubent incubent = new Incubent();
        incubent.setIncuname(incuname);
        incubent.setIncuId(incuId);
        incubent.setPw(passwordEncoder.encode(pw));
        incubent.setEmail(email);
        incubent.setAge(age);
        incubent.setCompanyin(companyin);

        this.incubentRepository.save(incubent);
        return incubent;
    }
}
