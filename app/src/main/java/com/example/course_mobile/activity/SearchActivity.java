package com.example.course_mobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.course_mobile.R;
import com.example.course_mobile.adapter.CourseAdapter;
import com.example.course_mobile.model.course.Course;
import com.example.course_mobile.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView rcvResultCourse;
    private TextView tvNumberResult;

    private List<Course> courseList;
    private CourseAdapter courseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initUI();
        addEvents();
    }

    private void addEvents() {

    }
    private void callApiSearch(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiService.apiService.listCourse(null)
                        .enqueue(new Callback<List<Course>>() {
                            @Override
                            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                                if(response.isSuccessful()){
                                    courseList = response.body();
                                    Log.e("data",courseList.get(0).getSubject());
                                    courseAdapter.setDataCourses(courseList);
                                    tvNumberResult.setText(courseList.size() +"");
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Course>> call, Throwable t) {
                                Log.e("loi",t.toString());
                            }
                        });
            }
        }).start();

    }
    private void initUI() {
        rcvResultCourse = findViewById(R.id.rcvResult);
        tvNumberResult = findViewById(R.id.tvNubberResult);
        courseList = new ArrayList<>();
        courseAdapter = new CourseAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rcvResultCourse.setLayoutManager(linearLayoutManager);
        rcvResultCourse.setAdapter(courseAdapter);
        callApiSearch();
    }
}