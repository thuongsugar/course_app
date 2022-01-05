package com.example.course_mobile.data_local;

import android.content.Context;
import android.util.Log;

import com.example.course_mobile.model.user.User;
import com.google.gson.Gson;

public class DataLocalManager {
    private static final String FIRST_INSTALLED = "FIRST_INSTALLED";
    private static final String USER_DATA = "USER_DATA";
    private static final String TOKEN_USER = "TOKEN";
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
    public static void setUser(User user){
        Gson gson = new Gson();
        String userStringJson = gson.toJson(user);
        DataLocalManager.getInstance().courseSharedPreferences.putStringValue(USER_DATA,userStringJson);
    }
    public static User getUser(){
        String jsonUser = DataLocalManager.getInstance().courseSharedPreferences.getStringValue(USER_DATA);
        Gson gson = new Gson();
        User u = gson.fromJson(jsonUser,User.class);
        return u;
    }
    public static void setToken(String token){
        DataLocalManager.getInstance().courseSharedPreferences.putStringValue(TOKEN_USER,"Token "+token);
    }
    public static String getToken(){
        return  DataLocalManager.getInstance().courseSharedPreferences.getStringValue(TOKEN_USER);
    }
}
