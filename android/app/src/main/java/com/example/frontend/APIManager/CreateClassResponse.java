package com.example.frontend.APIManager;

public class CreateClassResponse {
    String id;
    String name;
    String instructor_name;
    String course_no;
    String date_created;

    public CreateClassResponse(String id, String name, String instructor_name, String course_no, String date_created) {
        this.id = id;
        this.name = name;
        this.instructor_name = instructor_name;
        this.course_no = course_no;
        this.date_created = date_created;
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

    public String getInstructor_name() {
        return instructor_name;
    }

    public void setInstructor_name(String instructor_name) {
        this.instructor_name = instructor_name;
    }

    public String getCourse_no() {
        return course_no;
    }

    public void setCourse_no(String course_no) {
        this.course_no = course_no;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
