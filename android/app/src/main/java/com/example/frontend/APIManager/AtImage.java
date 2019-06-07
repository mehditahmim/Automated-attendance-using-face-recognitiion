package com.example.frontend.APIManager;

public class AtImage {
    String id;
    String img;
    String processed_img;
    String attendance;

    public AtImage(String id, String img, String processed_img, String attendance) {
        this.id = id;
        this.img = img;
        this.processed_img = processed_img;
        this.attendance = attendance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getProcessed_img() {
        return processed_img;
    }

    public void setProcessed_img(String processed_img) {
        this.processed_img = processed_img;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }
}
