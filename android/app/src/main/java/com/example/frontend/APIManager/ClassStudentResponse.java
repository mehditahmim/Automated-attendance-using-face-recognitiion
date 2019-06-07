package com.example.frontend.APIManager;

public class ClassStudentResponse {
    String id;
    String classroom;
    String student;

    public ClassStudentResponse(String id, String classroom, String student) {
        this.id = id;
        this.classroom = classroom;
        this.student = student;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }
}
