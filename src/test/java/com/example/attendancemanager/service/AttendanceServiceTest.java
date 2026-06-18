package com.example.attendancemanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.example.attendancemanager.entity.AppUser;
import com.example.attendancemanager.entity.AttendanceRecord;
import com.example.attendancemanager.exception.AccessDeniedException;
import com.example.attendancemanager.repository.AttendanceRepository;

class AttendanceServiceTest {

    private final AttendanceRepository attendanceRepository =
            mock(AttendanceRepository.class);

    private final AttendanceService attendanceService =
            new AttendanceService(attendanceRepository);

    @Test
    void getWorkingMinutesSubtractsBreakMinutes() {
        AttendanceRecord record = createRecord(9, 0, 18, 0, 60);

        long workingMinutes = record.getWorkingMinutes();

        assertThat(workingMinutes).isEqualTo(480);
    }

    @Test
    void getWorkingMinutesReturnsZeroWhenClockOutTimeIsMissing() {
        AttendanceRecord record = new AttendanceRecord();
        record.setClockInTime(LocalDateTime.of(2026, 6, 18, 9, 0));
        record.setBreakMinutes(60);

        long workingMinutes = record.getWorkingMinutes();

        assertThat(workingMinutes).isZero();
    }

    @Test
    void calculateTotalWorkingMinutesSumsRecords() {
        AttendanceRecord first = createRecord(9, 0, 18, 0, 60);
        AttendanceRecord second = createRecord(10, 0, 17, 0, 45);

        long totalMinutes = attendanceService.calculateTotalWorkingMinutes(
                List.of(first, second));

        assertThat(totalMinutes).isEqualTo(855);
    }

    @Test
    void findByIdAndUserReturnsRecordWhenOwnerMatches() {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("admin");

        AttendanceRecord record = new AttendanceRecord();
        record.setId(10L);
        record.setUser(user);

        when(attendanceRepository.findByIdAndUser(10L, user))
                .thenReturn(Optional.of(record));

        AttendanceRecord result = attendanceService.findByIdAndUser(10L, user);

        assertThat(result).isSameAs(record);
    }

    @Test
    void findByIdAndUserThrowsAccessDeniedWhenRecordIsNotFound() {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("admin");

        when(attendanceRepository.findByIdAndUser(99L, user))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> attendanceService.findByIdAndUser(99L, user))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void deleteDelegatesToRepository() {
        AttendanceRecord record = new AttendanceRecord();
        record.setId(10L);

        attendanceService.delete(record);

        verify(attendanceRepository).delete(record);
    }

    private AttendanceRecord createRecord(
            int clockInHour,
            int clockInMinute,
            int clockOutHour,
            int clockOutMinute,
            int breakMinutes) {

        AttendanceRecord record = new AttendanceRecord();
        record.setClockInTime(LocalDateTime.of(
                2026, 6, 18, clockInHour, clockInMinute));
        record.setClockOutTime(LocalDateTime.of(
                2026, 6, 18, clockOutHour, clockOutMinute));
        record.setBreakMinutes(breakMinutes);

        return record;
    }
}
