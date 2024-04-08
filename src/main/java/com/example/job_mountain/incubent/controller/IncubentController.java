package com.example.job_mountain.incubent.controller;

import com.example.job_mountain.incubent.service.IncubentService;
import com.example.job_mountain.user.domain.IncubentCreateForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/incubent")

public class IncubentController {

    private final IncubentService incubentService;

    @GetMapping("/signup")
    public String signup(IncubentCreateForm incubentCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid IncubentCreateForm incubentCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!incubentCreateForm.getPw1().equals(incubentCreateForm.getPw2())) {
            bindingResult.rejectValue("pw2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        incubentService.create(incubentCreateForm.getIncuname(), incubentCreateForm.getIncuId(),
                incubentCreateForm.getPw1(), incubentCreateForm.getEmail(), incubentCreateForm.getAge(), incubentCreateForm.getCompanyin());

        return "redirect:/";
    }
}