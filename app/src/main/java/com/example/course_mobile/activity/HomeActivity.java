package com.example.course_mobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.course_mobile.R;
import com.example.course_mobile.fragment.CourseFragment;
import com.example.course_mobile.fragment.ProfileFragment;
import com.example.course_mobile.fragment.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ActionBar actionBar;

    private boolean doubleBack = false;

    private final CourseFragment courseFragment = new CourseFragment();
    private final ProfileFragment profileFragment = new ProfileFragment();
    private final SettingFragment settingFragment = new SettingFragment();

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment active = courseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

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
                        loadFragment(courseFragment);
                        return true;
                    case R.id.btnProfile:
                        Log.e("vao","profile");
                        loadFragment(profileFragment);
                        return true;
                    case R.id.btnSetting:
                        Log.e("vao","setting");
                        loadFragment(settingFragment);
                        return true;
                }

                return false;
            }
        });

    }

    private void initUI() {

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, settingFragment, "3")
                .hide(settingFragment)
                .commit();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, profileFragment, "2")
                .hide(profileFragment)
                .commit();

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container,courseFragment, "1")
                .commit();


    }

    private void loadFragment(Fragment fragment) {


        fragmentManager.beginTransaction().hide(active).show(fragment).commit();
        active = fragment;

    }

    @Override
    public void onBackPressed() {
        if (active == courseFragment){
            if (doubleBack){
                super.onBackPressed();
            }else {
                Toast toast =  Toast.makeText(HomeActivity.this
                        ,getResources().getString(R.string.exit)
                        ,Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                doubleBack = true;
            }

        }else {
            loadFragment(courseFragment);
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }
    }
}