package com.example.attendancemanager.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.attendancemanager.entity.AppUser;
import com.example.attendancemanager.repository.AppUserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;

    public DataInitializer(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public void run(String... args) {
        if (appUserRepository.findByUsername("admin").isEmpty()) {
            AppUser user = new AppUser();
            user.setUsername("admin");
            user.setPassword("password");
            user.setRole("USER");
            user.setEnabled(true);

            appUserRepository.save(user);
        }
    }
}