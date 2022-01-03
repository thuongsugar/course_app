package com.example.course_mobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.course_mobile.R;
import com.example.course_mobile.fragment.CourseFragment;
import com.example.course_mobile.fragment.ProfileFragment;
import com.example.course_mobile.fragment.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ActionBar actionBar;

    private static final int FRAGMENT_COURSE = 0;
    private static final int FRAGMENT_PROFILE = 1;
    private static final int FRAGMENT_SETTING = 2;

    private int currentFragment =FRAGMENT_COURSE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        actionBar = getSupportActionBar();
        initUI();
        addEvent();
    }

    private void addEvent() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()){
                    case R.id.btnCourse:
                        Log.e("vao","course");
                        openCourseFragment();
                        return true;
                    case R.id.btnProfile:
                        Log.e("vao","profile");

                        actionBar.setTitle("aloo");
                        openProfileFragment();

                        return true;
                    case R.id.btnSetting:
                        Log.e("vao","setting");

                        actionBar.setTitle(getResources().getString(R.string.bottom_setting));
                        openSettingFragment();
                        return true;
                }

                return false;
            }
        });
    }

    private void initUI() {

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        loadFragment(new CourseFragment());
    }

    private void openCourseFragment(){
        if(currentFragment != FRAGMENT_COURSE){
            loadFragment(new CourseFragment());
            currentFragment = FRAGMENT_COURSE;
        }
    }
    private void openProfileFragment(){
        if(currentFragment != FRAGMENT_PROFILE){
            loadFragment(new ProfileFragment());
            currentFragment = FRAGMENT_PROFILE;
        }
    }
    private void openSettingFragment(){
        if(currentFragment != FRAGMENT_SETTING){
            loadFragment(new SettingFragment());
            currentFragment = FRAGMENT_SETTING;
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}