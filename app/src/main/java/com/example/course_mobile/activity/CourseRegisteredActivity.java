package com.example.course_mobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.course_mobile.R;
import com.example.course_mobile.adapter.CourseAdapter;
import com.example.course_mobile.data_local.DataLocalManager;
import com.example.course_mobile.model.course.Course;
import com.example.course_mobile.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseRegisteredActivity extends AppCompatActivity {
    private static final int COURSE_DATA = 1;
    private static final int COURSE_401 = 0;
    private RecyclerView rcvCourseRegistered;
    private CourseAdapter courseAdapter;
    private List<Course> courseList;

    private LinearLayout layoutNotFound;
    private ProgressBar pgCourseRegistered;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_registered);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.registered));

        initHandle();
        initUI();
        addEvents();
    }

    private void initHandle() {
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == COURSE_DATA){
                    courseList = (List<Course>) msg.obj;
                    if (courseList.size() == 0){
                        layoutNotFound.setVisibility(View.VISIBLE);
                        rcvCourseRegistered.setVisibility(View.GONE);
                    }
                    courseAdapter.setDataCourses(courseList);
                    pgCourseRegistered.setVisibility(View.GONE);
                }else if(msg.what == COURSE_401){
                    pgCourseRegistered.setVisibility(View.GONE);
                    startActivity(new Intent(CourseRegisteredActivity.this, LoginActivity.class));
                    finish();
                }
                super.handleMessage(msg);
            }
        };
    }

    private void addEvents() {
        courseAdapter.onClickCourse(new CourseAdapter.OnClickCourse() {
            @Override
            public void onClick(Course courseClicked) {
                handleClickCourse(courseClicked);
            }
        });
    }

    private void handleClickCourse(Course courseClicked) {
        Intent intentChoiceLesson = new Intent(CourseRegisteredActivity.this,ChoiceLessonActivity.class);
        intentChoiceLesson.putExtra(CourseDetailActivity.COURSE_DATA, courseClicked);
        startActivity(intentChoiceLesson);
    }

    private void initUI() {
        layoutNotFound = findViewById(R.id.layoutNotCourse);
        pgCourseRegistered = findViewById(R.id.pgCourseRegistered);
        pgCourseRegistered.setVisibility(View.VISIBLE);
        rcvCourseRegistered = findViewById(R.id.rcvCourseRegistered);
        courseAdapter = new CourseAdapter();
        courseList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rcvCourseRegistered.setLayoutManager(linearLayoutManager);
        rcvCourseRegistered.setAdapter(courseAdapter);
        callApiGetCourseRegistered();
    }

    private void callApiGetCourseRegistered() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiService.apiService.getCourseRegistered(DataLocalManager.getToken())
                        .enqueue(new Callback<List<Course>>() {
                            @Override
                            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                                handleResponse(response);
                            }

                            @Override
                            public void onFailure(Call<List<Course>> call, Throwable t) {
                                pgCourseRegistered.setVisibility(View.GONE);
                                Log.e("Loi goi api registered",t.toString());
                            }
                        });
            }

            private void handleResponse(Response<List<Course>> response) {
                if (response.isSuccessful()){
                    List<Course> courseList = response.body();
                    Message message = new Message();
                    message.what = COURSE_DATA;
                    message.obj = courseList;
                    handler.sendMessage(message);
                }else if (response.code() == 401){
                    handler.sendEmptyMessage(COURSE_401);
                }else {
                    pgCourseRegistered.setVisibility(View.GONE);
                    Toast.makeText(CourseRegisteredActivity.this
                            ,getResources().getString(R.string.error_server)
                            ,Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}