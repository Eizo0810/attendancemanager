package com.example.attendancemanager.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        validateBreakMinutes(attendanceRecord);

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

    public AttendanceRecord findByIdAndUser(Long id, AppUser user) {
        return attendanceRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "この勤怠記録にアクセスする権限がありません。"));
    }

    public void delete(AttendanceRecord attendanceRecord) {
        attendanceRepository.delete(attendanceRecord);
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
    
    public long calculateTotalWorkingMinutes(List<AttendanceRecord> records) {
        return records.stream()
                .mapToLong(AttendanceRecord::getWorkingMinutes)
                .sum();
    }

    private void validateBreakMinutes(AttendanceRecord attendanceRecord) {
        Integer breakMinutes = attendanceRecord.getBreakMinutes();

        if (breakMinutes == null) {
            return;
        }

        if (breakMinutes < 0) {
            throw new IllegalArgumentException(
                    "休憩時間は0分以上で入力してください。");
        }

        if (attendanceRecord.getClockInTime() == null
                || attendanceRecord.getClockOutTime() == null) {
            return;
        }

        long elapsedMinutes = Duration.between(
                attendanceRecord.getClockInTime(),
                attendanceRecord.getClockOutTime()).toMinutes();

        if (breakMinutes > elapsedMinutes) {
            throw new IllegalArgumentException(
                    "休憩時間は勤務時間以内で入力してください。");
        }
    }
}
