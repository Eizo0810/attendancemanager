package com.example.attendancemanager.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.attendancemanager.entity.AppUser;
import com.example.attendancemanager.entity.AttendanceRecord;
import com.example.attendancemanager.repository.AppUserRepository;
import com.example.attendancemanager.service.AttendanceService;

@Controller
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final AppUserRepository appUserRepository;

    public AttendanceController(
            AttendanceService attendanceService,
            AppUserRepository appUserRepository) {

        this.attendanceService = attendanceService;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/attendance")
    public String index(
            Model model,
            Principal principal) {

        AppUser user =
                appUserRepository
                        .findByUsername(principal.getName())
                        .orElseThrow();

        model.addAttribute(
                "records",
                attendanceService.findByUser(user));

        return "attendance/index";
    }
    
    @PostMapping("/attendance/clock-in")
    public String clockIn(Principal principal) {

        AppUser user = appUserRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        AttendanceRecord record = new AttendanceRecord();

        record.setUser(user);
        record.setWorkDate(LocalDate.now());
        record.setClockInTime(LocalDateTime.now());
        record.setBreakMinutes(0);

        attendanceService.save(record);

        return "redirect:/attendance";
    }

    @PostMapping("/attendance/clock-out")
    public String clockOut(Principal principal) {

        AppUser user = appUserRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        AttendanceRecord record = attendanceService.findTodayRecord(user)
                .orElseThrow();

        record.setClockOutTime(LocalDateTime.now());

        attendanceService.save(record);

        return "redirect:/attendance";
    }
}