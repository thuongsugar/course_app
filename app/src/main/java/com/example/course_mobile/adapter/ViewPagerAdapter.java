package com.example.course_mobile.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.course_mobile.onbroading.OnboardingFrament1;
import com.example.course_mobile.onbroading.OnboardingFrament2;
import com.example.course_mobile.onbroading.OnboardingFrament3;
import com.example.course_mobile.onbroading.OnboardingFrament4;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new OnboardingFrament1();
            case 1:
                return new OnboardingFrament2();
            case 2:
                return new OnboardingFrament3();
            case 3:
                return new OnboardingFrament4();
            default:
                return new OnboardingFrament1();

        }


    }

    @Override
    public int getCount() {
        return 4;
    }
}
