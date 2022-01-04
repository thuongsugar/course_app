package com.example.course_mobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class SearchActivity extends AppCompatActivity {
    private RecyclerView rcvResultCourse;
    private TextView tvNumberResult;
    private ProgressBar pgBarResult;

    private List<Course> courseList;
    private CourseAdapter courseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar actionBar = getSupportActionBar();
        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        initUI();
        addEvents();
    }

    private void addEvents() {

    }
    private void callApiSearch(){
        pgBarResult.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiService.apiService.listCourse(DataLocalManager.getToken(),null)
                        .enqueue(new Callback<List<Course>>() {
                            @Override
                            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                                if(response.isSuccessful()){
                                    courseList = response.body();
                                    courseAdapter.setDataCourses(courseList);
                                    tvNumberResult.setText(courseList.size() +"");
                                    if(courseList.size() == 0){
                                        // TODO: 04/01/2022  show not found 
                                    }
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
        rcvResultCourse = findViewById(R.id.rcvResult);
        tvNumberResult = findViewById(R.id.tvNubberResult);
        pgBarResult = findViewById(R.id.pgBarResult);

        courseList = new ArrayList<>();
        courseAdapter = new CourseAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rcvResultCourse.setLayoutManager(linearLayoutManager);
        rcvResultCourse.setAdapter(courseAdapter);
        callApiSearch();

    }
}