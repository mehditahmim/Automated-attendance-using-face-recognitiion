package com.example.frontend.APIManager;

import java.util.HashMap;


public class DataHolder {
    private String TOKEN;
    private String ID;
    private String class_id;
    private String at_id;
    private LandingPageResponse LPR;
    private HashMap<String, ClassResponse> class_list_teacher = new HashMap<>();
    private String currentAttendanceKey;
    private String currentClassKey;
    private ClassResponse classResponse;


    public DataHolder() {
    }

    public String getAt_id() {
        return at_id;
    }

    public void setAt_id(String at_id) {
        this.at_id = at_id;
    }

    public DataHolder(String TOKEN, String ID, String class_id, String at_id, LandingPageResponse LPR, HashMap<String, ClassResponse> class_list_teacher, String currentAttendanceKey, String currentClassKey, ClassResponse classResponse) {
        this.TOKEN = TOKEN;
        this.ID = ID;
        this.class_id = class_id;
        this.at_id = at_id;
        this.LPR = LPR;
        this.class_list_teacher = class_list_teacher;
        this.currentAttendanceKey = currentAttendanceKey;
        this.currentClassKey = currentClassKey;
        this.classResponse = classResponse;
    }

    public ClassResponse getClassResponse() {
        return classResponse;
    }

    public void setClassResponse(ClassResponse classResponse) {
        this.classResponse = classResponse;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public LandingPageResponse getLPR() {
        return LPR;
    }

    public void setLPR(LandingPageResponse LPR) {
        this.LPR = LPR;
    }

    public HashMap<String, ClassResponse> getClass_list_teacher() {
        return class_list_teacher;
    }

    public void setClass_list_teacher(HashMap<String, ClassResponse> class_list_teacher) {
        this.class_list_teacher = class_list_teacher;
    }

    public String getCurrentAttendanceKey() {
        return currentAttendanceKey;
    }

    public void setCurrentAttendanceKey(String currentAttendanceKey) {
        this.currentAttendanceKey = currentAttendanceKey;
    }

    public String getCurrentClassKey() {
        return currentClassKey;
    }

    public void setCurrentClassKey(String currentClassKey) {
        this.currentClassKey = currentClassKey;
    }
}