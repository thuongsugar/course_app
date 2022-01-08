package com.example.course_mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.example.course_mobile.R;
import com.example.course_mobile.activity.CourseRegisteredActivity;


public class ProfileFragment extends Fragment {
    private ImageView imvAvatar;
    private TextView tvCourseRegistered,tvLogout;
    private View view;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        initUI();
        addEvents();
        return view;
    }

    private void addEvents() {
        tvCourseRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CourseRegisteredActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initUI() {
        tvCourseRegistered = view.findViewById(R.id.tvCourseRegistered);
        tvLogout = view.findViewById(R.id.tvLogout);
        imvAvatar = view.findViewById(R.id.imvAvatar);
    }
}