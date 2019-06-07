package com.example.frontend.APIManager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.frontend.APIManager.ClassResponse;
import com.example.frontend.APIManager.CreateClassResponse;
import com.example.frontend.APIManager.LandingPageResponse;

import java.util.HashMap;

public class CacheValues {
    public static String TOKEN= "";
    public static String ID= "";
    public static LandingPageResponse LPR;
    public static HashMap<String, ClassResponse> class_list_teacher=new HashMap<>();
    public static String currentAttendanceKey;
    public static String currentClassKey;



}

