package com.example.frontend.APIManager;

import java.util.ArrayList;

public class ExAttendanceResponse {
    String id;
    String class_room;
    String date_created;
    int num_present;

    ArrayList<AtImage> attendance_image;
    ArrayList<AtStudent> attendance_student;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClass_room() {
        return class_room;
    }

    public void setClass_room(String class_room) {
        this.class_room = class_room;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public int getNum_present() {
        return num_present;
    }

    public void setNum_present(int num_present) {
        this.num_present = num_present;
    }

    public ArrayList<AtImage> getAttendance_image() {
        return attendance_image;
    }

    public void setAttendance_image(ArrayList<AtImage> attendance_image) {
        this.attendance_image = attendance_image;
    }

    public ArrayList<AtStudent> getAttendance_student() {
        return attendance_student;
    }

    public void setAttendance_student(ArrayList<AtStudent> attendance_student) {
        this.attendance_student = attendance_student;
    }

    public ExAttendanceResponse(String id, String class_room, String date_created, int num_present, ArrayList<AtImage> attendance_image, ArrayList<AtStudent> attendance_student) {
        this.id = id;
        this.class_room = class_room;
        this.date_created = date_created;
        this.num_present = num_present;
        this.attendance_image = attendance_image;
        this.attendance_student = attendance_student;
    }
}
