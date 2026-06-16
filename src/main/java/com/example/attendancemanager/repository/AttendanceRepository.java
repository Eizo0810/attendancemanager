package com.example.attendancemanager.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendancemanager.entity.AppUser;
import com.example.attendancemanager.entity.AttendanceRecord;

public interface AttendanceRepository
extends JpaRepository<AttendanceRecord, Long> {
	
	Optional<AttendanceRecord> findByUserAndWorkDate(
	        AppUser user,
	        LocalDate workDate);
	
	List<AttendanceRecord> findByUser(AppUser user);
}
