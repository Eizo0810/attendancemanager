package com.example.attendancemanager.exception;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
        super("この勤怠記録にアクセスする権限がありません。");
    }
}
