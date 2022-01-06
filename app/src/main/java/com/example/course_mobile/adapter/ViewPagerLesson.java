package com.example.course_mobile.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.course_mobile.fragment.LessonFragment;
import com.example.course_mobile.fragment.QuizFragment;

public class ViewPagerLesson extends FragmentStateAdapter {
    private final Bundle bundle;

    public ViewPagerLesson(@NonNull FragmentActivity fragmentActivity,Bundle data) {
        super(fragmentActivity);
        this.bundle = data;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(position == 1){
            final QuizFragment quizFragment = new QuizFragment();
            quizFragment.setArguments(this.bundle);
            return quizFragment;
        }
        final LessonFragment lessonFragment = new LessonFragment();
        lessonFragment.setArguments(this.bundle);
        return lessonFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
