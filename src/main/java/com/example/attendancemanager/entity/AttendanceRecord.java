package com.example.attendancemanager.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "attendance_records")
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate workDate;

    private LocalDateTime clockInTime;

    private LocalDateTime clockOutTime;

    private Integer breakMinutes;

    private String note;

    public AttendanceRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public LocalDateTime getClockInTime() {
        return clockInTime;
    }

    public void setClockInTime(LocalDateTime clockInTime) {
        this.clockInTime = clockInTime;
    }

    public LocalDateTime getClockOutTime() {
        return clockOutTime;
    }

    public void setClockOutTime(LocalDateTime clockOutTime) {
        this.clockOutTime = clockOutTime;
    }

    public Integer getBreakMinutes() {
        return breakMinutes;
    }

    public void setBreakMinutes(Integer breakMinutes) {
        this.breakMinutes = breakMinutes;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}