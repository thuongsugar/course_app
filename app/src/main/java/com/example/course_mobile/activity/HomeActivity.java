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
                        loadFragment(courseFragment,"1",0);
                        return true;
                    case R.id.btnProfile:
                        Log.e("vao","profile");
                        actionBar.setTitle("aloo");
                        loadFragment(profileFragment,"2",1);
                        return true;
                    case R.id.btnSetting:
                        Log.e("vao","setting");
                        actionBar.setTitle(getResources().getString(R.string.bottom_setting));
                        loadFragment(settingFragment,"3",2);
                        return true;
                }

                return false;
            }
        });

    }

    private void initUI() {

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        loadFragment(courseFragment,"1",0);
        loadFragment(profileFragment,"2",1);
        loadFragment(settingFragment,"3",2);


    }

    private void loadFragment(Fragment fragment,String tag,int position) {
        if (fragment.isAdded()){
            Log.e("added",fragment.getTag());
            fragmentManager.beginTransaction().hide(active).show(fragment).commit();
        }else {

            fragmentManager.beginTransaction().add(R.id.fragment_container,fragment,tag).commit();
        }
        bottomNavigationView.getMenu().getItem(position).setChecked(true);
        active = fragment;
        Log.e("active",active.getTag());
    }

    @Override
    public void onBackPressed() {
        if (active == courseFragment){
            if (doubleBack){
                super.onBackPressed();
            }
            Toast toast =  Toast.makeText(HomeActivity.this,"Nhấn back thêm 1 lần nữa để thoát",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();

        }else {
            loadFragment(courseFragment,"1",0);
        }
    }
}