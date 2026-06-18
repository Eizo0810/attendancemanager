package com.example.attendancemanager.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.attendancemanager.entity.AppUser;
import com.example.attendancemanager.repository.AppUserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        appUserRepository.findByUsername("admin").ifPresentOrElse(user -> {
            if (user.getPassword() == null || !user.getPassword().startsWith("$2")) {
                user.setPassword(passwordEncoder.encode("password"));
            }
            user.setRole("ADMIN");
            user.setEnabled(true);
            appUserRepository.save(user);
        }, () -> {
            AppUser user = new AppUser();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole("ADMIN");
            user.setEnabled(true);

            appUserRepository.save(user);
        });
    }
}
