package com.example.course_mobile.data_local;

import android.content.Context;

public class DataLocalManager {
    private static final String FIRST_INSTALLED = "FIRST_INSTALLED";
    private static DataLocalManager instance;
    private CourseSharedPreferences courseSharedPreferences;

    public static void init(Context context){
        instance = new DataLocalManager();
        instance.courseSharedPreferences = new CourseSharedPreferences(context);
    }
    public static DataLocalManager getInstance(){
        if(instance == null){
            instance = new DataLocalManager();
        }
        return instance;
    }
    public static void setFirstInstalled(Boolean value){
        DataLocalManager.getInstance().courseSharedPreferences.putBooleValue(FIRST_INSTALLED,value);
    }
    public static boolean getFirstInstalled(){
        return  DataLocalManager.getInstance().courseSharedPreferences.getBooleanValue(FIRST_INSTALLED);
    }
}
