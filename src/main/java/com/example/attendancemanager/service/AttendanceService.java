package com.example.attendancemanager.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.attendancemanager.entity.AppUser;
import com.example.attendancemanager.entity.AttendanceRecord;
import com.example.attendancemanager.repository.AttendanceRepository;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public List<AttendanceRecord> findAll() {
        return attendanceRepository.findAll();
    }

    public AttendanceRecord save(AttendanceRecord attendanceRecord) {
        return attendanceRepository.save(attendanceRecord);
    }
    
    public Optional<AttendanceRecord> findTodayRecord(AppUser user) {
        return attendanceRepository.findByUserAndWorkDate(user, LocalDate.now());
    }
    
    public List<AttendanceRecord> findByUser(AppUser user) {
        return attendanceRepository.findByUser(user);
    }
    
    public AttendanceRecord findById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow();
    }
    
    public List<AttendanceRecord> findByUserAndWorkDateBetween(
            AppUser user,
            LocalDate startDate,
            LocalDate endDate) {

        return attendanceRepository.findByUserAndWorkDateBetween(
                user,
                startDate,
                endDate);
    }
}