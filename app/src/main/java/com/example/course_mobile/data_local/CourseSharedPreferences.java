package com.example.course_mobile.data_local;

import android.content.Context;
import android.content.SharedPreferences;

public class CourseSharedPreferences {
    private static final String MY_SHARE_PREFERENCES = "COURSE_SHARE_PREFERENCES";
    private Context context;

    public CourseSharedPreferences(Context context) {
        this.context = context;
    }
    public void putBooleValue(String key,Boolean value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARE_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }
    public boolean getBooleanValue(String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARE_PREFERENCES,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }
}
