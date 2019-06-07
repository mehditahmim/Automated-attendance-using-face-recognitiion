package com.example.frontend.APIManager;

import java.io.Serializable;
import java.util.ArrayList;

public class LandingPageResponse implements Serializable {
    String id;
    String username;
    String first_name;
    String last_name;
    boolean is_student;
    String picture;
    ArrayList<String> classrooms;
    public LandingPageResponse() {

    }

    public LandingPageResponse(String id, String username, String first_name, String last_name, boolean is_student, String picture, ArrayList<String> classrooms) {
        this.id = id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.is_student = is_student;
        this.picture = picture;
        this.classrooms = classrooms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public boolean getIs_student() {
        return is_student;
    }

    public void setIs_student(boolean is_student) {
        this.is_student = is_student;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public ArrayList<String> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(ArrayList<String> classrooms) {
        this.classrooms = classrooms;
    }
}

