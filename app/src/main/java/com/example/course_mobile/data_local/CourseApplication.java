package com.example.course_mobile.data_local;

import android.app.Application;

public class CourseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManager.init(getApplicationContext());
    }
}
