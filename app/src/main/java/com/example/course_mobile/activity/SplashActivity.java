package com.example.course_mobile.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.course_mobile.R;
import com.example.course_mobile.data_local.DataLocalManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(DataLocalManager.getFirstInstall()){
            startActivity(new Intent(SplashActivity.this,HomeActivity.class));
        }else {
            Intent intent = new Intent(SplashActivity.this, OnbroadingActivity.class);
            startActivity(intent);
        }
        finish();
    }
}