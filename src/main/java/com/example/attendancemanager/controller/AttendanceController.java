package com.example.attendancemanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}