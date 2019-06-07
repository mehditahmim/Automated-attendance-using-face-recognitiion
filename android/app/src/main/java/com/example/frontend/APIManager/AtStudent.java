package com.example.frontend.APIManager;

public class AtStudent {
    String id;
    String student;
    String attendance;

    public AtStudent(String id, String student, String attendance) {
        this.id = id;
        this.student = student;
        this.attendance = attendance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }
}
