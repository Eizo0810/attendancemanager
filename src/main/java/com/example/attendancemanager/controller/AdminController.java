package com.example.attendancemanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.attendancemanager.repository.AppUserRepository;

@Controller
public class AdminController {

    private final AppUserRepository appUserRepository;

    public AdminController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/admin/users")
    public String users(Model model) {
        model.addAttribute("users", appUserRepository.findAll());

        return "admin/users";
    }
}
