package com.example.attendancemanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
}