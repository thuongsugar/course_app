package com.example.course_mobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.course_mobile.R;
import com.example.course_mobile.fragment.LessonFragment;
import com.example.course_mobile.model.lesson.Lesson;

public class LessonCourseActivity extends AppCompatActivity {
    private TextView tvSubject,tvContent;
    private Lesson lessonClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_lesson);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        initUI();
        addEvents();
    }

    private void addEvents() {

    }

    private void initUI() {
        lessonClicked = (Lesson) getIntent().getExtras()
                .get(LessonFragment.LESSON_CHOICE);
        tvContent = findViewById(R.id.tvLessonContent);
        tvSubject = findViewById(R.id.tvSubject);

        tvSubject.setText(lessonClicked.getSubject());
        tvContent.setText(lessonClicked.getContent());
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}