package com.example.course_mobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.course_mobile.R;
import com.example.course_mobile.adapter.LessonAdapter;
import com.example.course_mobile.adapter.ViewPagerLesson;
import com.example.course_mobile.data_local.DataLocalManager;
import com.example.course_mobile.model.course.Course;
import com.example.course_mobile.model.lesson.Lesson;
import com.example.course_mobile.service.ApiService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoiceLessonActivity extends AppCompatActivity {
    public static final String COURSE_ID = "DATA_ID_COURSE";
    public static final String COURSE_IMAGE = "course_image";

    private ImageView imvChoiceLesson;
    private TextView tvTitleChoiceLesson,tvDesChoiceLesson;


    private TabLayout tabLayoutLesson;
    private ViewPager2 viewPagerLesson;
    private ViewPagerLesson adapterViewPager;

    private Course courseDetail;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_lesson);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        initUI();
        addEvents();
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addEvents() {

    }

    private void initUI() {
        bundle = new Bundle();
        tvTitleChoiceLesson = findViewById(R.id.tvTitleChoiceLesson);
        tvDesChoiceLesson = findViewById(R.id.tvDesChoiceLesson);

        tabLayoutLesson = findViewById(R.id.tabLayoutLesson);
        viewPagerLesson = findViewById(R.id.vpLesson);

        getBundle();
        Bundle bundle = new Bundle();
        bundle.putInt(COURSE_ID,courseDetail.getId());
        bundle.putString(COURSE_IMAGE,courseDetail.getImage());
        adapterViewPager = new ViewPagerLesson(this,bundle);

        viewPagerLesson.setAdapter(adapterViewPager);
        new TabLayoutMediator(tabLayoutLesson, viewPagerLesson, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 1){
                    tab.setText("Quiz");
                }else {
                    tab.setText("Lesson");
                }
            }
        }).attach();


//        imvChoiceLesson = findViewById(R.id.imvChoiceLesson);




    }

    private void getBundle() {
        courseDetail = (Course) getIntent().getExtras().get(CourseDetailActivity.COURSE_DATA);
//        Glide.with(this)
//                .load(courseDetail.getImage())
//                .centerCrop()
//                .placeholder(R.drawable.course_defaut)
//                .into(imvChoiceLesson);
        tvTitleChoiceLesson.setText(courseDetail.getSubject());
        tvDesChoiceLesson.setText(courseDetail.getDes());

        //gui dulieu qua fragment
//        adapterViewPager.setFragmentData(courseDetail.getId());
        
    }



}