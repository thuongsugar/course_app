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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.course_mobile.R;
import com.example.course_mobile.adapter.CourseAdapter;
import com.example.course_mobile.data_local.DataLocalManager;
import com.example.course_mobile.fragment.CourseFragment;
import com.example.course_mobile.model.category.Category;
import com.example.course_mobile.model.course.Course;
import com.example.course_mobile.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    public static final String COURSE_OBJ = "COURSE_OBJ";
    private static final int DATA_COURSES = 0;
    private RecyclerView rcvResultCourse;
    private TextView tvNumberResult;
    private ProgressBar pgBarResult;

    private Handler handler;

    private List<Course> courseList;
    private CourseAdapter courseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar actionBar = getSupportActionBar();
        initHandle();
        initUI();
        addEvents();
    }

    private void initHandle() {
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == DATA_COURSES){
                    courseList = (List<Course>) msg.obj;
                    courseAdapter.setDataCourses(courseList);
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
        Intent intentCourseDetail = new Intent(SearchActivity.this,CourseDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(COURSE_OBJ,courseClicked);
        intentCourseDetail.putExtras(bundle);
        startActivity(intentCourseDetail);
    }

    private void callApiGetCourse(int idCategory){
        pgBarResult.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiService.apiService.listCourse(DataLocalManager.getToken(),null,idCategory)
                        .enqueue(new Callback<List<Course>>() {
                            @Override
                            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                                if(response.isSuccessful()){
                                    List<Course> courseList = response.body();
                                    Message message = new Message();
                                    message.what = DATA_COURSES;
                                    message.obj = courseList;
                                    handler.sendMessage(message);

                                }

                                pgBarResult.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<List<Course>> call, Throwable t) {
                                Log.e("loi",t.toString());
                                pgBarResult.setVisibility(View.GONE);
                            }
                        });
            }
        }).start();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initUI() {
        Category categorySelected = (Category) getIntent().getExtras().get(CourseFragment.TAG_CATEGORY);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(categorySelected.getName());

        rcvResultCourse = findViewById(R.id.rcvResult);
        tvNumberResult = findViewById(R.id.tvNubberResult);

        pgBarResult = findViewById(R.id.pgBarResult);

        courseList = new ArrayList<>();
        courseAdapter = new CourseAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rcvResultCourse.setLayoutManager(linearLayoutManager);
        rcvResultCourse.setAdapter(courseAdapter);


        callApiGetCourse(categorySelected.getId());

    }
}