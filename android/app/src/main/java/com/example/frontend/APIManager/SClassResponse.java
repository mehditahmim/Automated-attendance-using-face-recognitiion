package com.example.frontend.APIManager;

import java.util.ArrayList;

public class SClassResponse {
    String id;
    String name;
    String course_no;

    public SClassResponse(String id, String name, String course_no) {
        this.id = id;
        this.name = name;
        this.course_no = course_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse_no() {
        return course_no;
    }

    public void setCourse_no(String course_no) {
        this.course_no = course_no;
    }
}
