package com.example.course_mobile.onbroading;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.course_mobile.MainActivity;
import com.example.course_mobile.R;
import com.example.course_mobile.activity.HomeActivity;
import com.example.course_mobile.data_local.DataLocalManager;


public class OnboardingFrament4 extends Fragment {
    private Button btnGetStart;
    private View mView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_onboarding_frament4, container, false);
        btnGetStart = mView.findViewById(R.id.btn_get_start);
        btnGetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataLocalManager.setFirstInstall(true);
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });
        return mView;
    }
}