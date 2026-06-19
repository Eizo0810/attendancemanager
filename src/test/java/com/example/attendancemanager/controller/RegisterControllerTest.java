package com.example.attendancemanager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.attendancemanager.config.SecurityConfig;
import com.example.attendancemanager.entity.AppUser;
import com.example.attendancemanager.repository.AppUserRepository;

@WebMvcTest(RegisterController.class)
@Import(SecurityConfig.class)
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppUserRepository appUserRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    void registerCreatesUserWhenInputIsValid() throws Exception {
        when(appUserRepository.findByUsername("newuser"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123"))
                .thenReturn("encoded-password");

        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", " newuser ")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));

        verify(appUserRepository).save(any(AppUser.class));
    }

    @Test
    void registerShowsErrorWhenUsernameAlreadyExists() throws Exception {
        AppUser existingUser = new AppUser();
        existingUser.setUsername("admin");

        when(appUserRepository.findByUsername("admin"))
                .thenReturn(Optional.of(existingUser));

        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", "admin")
                .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));

        verify(appUserRepository, never()).save(any(AppUser.class));
    }

    @Test
    void registerShowsErrorWhenPasswordIsTooShort() throws Exception {
        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", "newuser")
                .param("password", "short"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));

        verify(appUserRepository, never()).save(any(AppUser.class));
    }
}
