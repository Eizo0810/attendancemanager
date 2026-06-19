package com.example.attendancemanager.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.attendancemanager.entity.AppUser;
import com.example.attendancemanager.repository.AppUserRepository;

@Controller
public class RegisterController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(String username, String password, Model model) {

        String normalizedUsername = username == null ? "" : username.trim();

        if (normalizedUsername.isBlank() || password == null || password.isBlank()) {
            model.addAttribute("error", "ユーザー名とパスワードを入力してください。");
            model.addAttribute("username", normalizedUsername);
            return "register";
        }

        if (password.length() < 8) {
            model.addAttribute("error", "パスワードは8文字以上で入力してください。");
            model.addAttribute("username", normalizedUsername);
            return "register";
        }

        if (appUserRepository.findByUsername(normalizedUsername).isPresent()) {
            model.addAttribute("error", "このユーザー名はすでに使われています。");
            model.addAttribute("username", normalizedUsername);
            return "register";
        }

        AppUser user = new AppUser();
        user.setUsername(normalizedUsername);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        user.setEnabled(true);

        appUserRepository.save(user);

        return "redirect:/login?registered";
    }
}
