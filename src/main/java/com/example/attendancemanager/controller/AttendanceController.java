package com.example.attendancemanager.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.attendancemanager.entity.AttendanceRecord;
import com.example.attendancemanager.service.AttendanceService;

@Controller
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/attendance")
    public String index(Model model) {
        model.addAttribute("records", attendanceService.findAll());
        return "attendance/index";
    }
    
    @PostMapping("/attendance/clock-in")
    public String clockIn() {

        AttendanceRecord record = new AttendanceRecord();
        record.setWorkDate(LocalDate.now());
        record.setClockInTime(LocalDateTime.now());
        record.setBreakMinutes(0);

        attendanceService.save(record);

        return "redirect:/attendance";
    }
}