package com.example.attendancemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendancemanager.entity.AttendanceRecord;

public interface AttendanceRepository
extends JpaRepository<AttendanceRecord, Long> {
}
