package com.example.attendancemanager.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            Principal principal,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        AppUser user = appUserRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        List<AttendanceRecord> records;

        if (startDate != null && endDate != null) {
            records = attendanceService.findByUserAndWorkDateBetween(
                    user,
                    startDate,
                    endDate);
        } else {
            records = attendanceService.findByUser(user);
        }

        long totalWorkingMinutes =
                attendanceService.calculateTotalWorkingMinutes(records);

        model.addAttribute("records", records);
        model.addAttribute("totalWorkingHours", totalWorkingMinutes / 60);
        model.addAttribute("remainingMinutes", totalWorkingMinutes % 60);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "attendance/index";
    }
    
    @GetMapping("/attendance/{id}/edit")
    public String editForm(
            @PathVariable Long id,
            Model model,
            Principal principal) {

        AppUser user = appUserRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        AttendanceRecord record = attendanceService.findByIdAndUser(id, user);

        model.addAttribute("record", record);

        return "attendance/edit";
    }
    
    @PostMapping("/attendance/clock-in")
    public String clockIn(Principal principal) {

        AppUser user = appUserRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        if (attendanceService.findTodayRecord(user).isPresent()) {
            return "redirect:/attendance?clockInExists";
        }

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
                .orElse(null);

        if (record == null) {
            return "redirect:/attendance?noClockIn";
        }

        if (record.getClockOutTime() != null) {
            return "redirect:/attendance?clockOutExists";
        }

        record.setClockOutTime(LocalDateTime.now());

        attendanceService.save(record);

        return "redirect:/attendance";
    }
    
    @PostMapping("/attendance/{id}/edit")
    public String updateBreakMinutes(
            @PathVariable Long id,
            @RequestParam Integer breakMinutes,
            @RequestParam(required = false) String note,
            Principal principal) {

        AppUser user = appUserRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        AttendanceRecord record = attendanceService.findByIdAndUser(id, user);

        record.setBreakMinutes(breakMinutes);
        record.setNote(note);

        attendanceService.save(record);

        return "redirect:/attendance";
    }

    @PostMapping("/attendance/{id}/delete")
    public String delete(
            @PathVariable Long id,
            Principal principal) {

        AppUser user = appUserRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        AttendanceRecord record = attendanceService.findByIdAndUser(id, user);

        attendanceService.delete(record);

        return "redirect:/attendance?deleted";
    }
}
