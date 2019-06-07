package com.example.frontend.APIManager;

public class DayAttendance {
    String day;
    String status;

    public DayAttendance(String day, String status) {
        this.day = day;
        this.status = status;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
