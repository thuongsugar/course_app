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
import com.example.course_mobile.activity.LoginActivity;
import com.example.course_mobile.data_local.DataLocalManager;
import com.example.course_mobile.model.user.User;
import com.example.course_mobile.service.ApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {
    private ImageView imvAvatar;
    private TextView tvCourseRegistered,tvLogout;
    private View view;
    private User currentUser;

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

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser.getId() != -1){
                    callApiLogout();

                }else {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void callApiLogout() {
        ApiService.apiService.userLogout(DataLocalManager.getToken())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        DataLocalManager.setUser(null);
                        DataLocalManager.setToken(null);
                        currentUser = DataLocalManager.getUser();
                        tvLogout.setText(getResources().getString(R.string.login));
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    private void initUI() {
        currentUser = DataLocalManager.getUser();
        tvCourseRegistered = view.findViewById(R.id.tvCourseRegistered);
        tvLogout = view.findViewById(R.id.tvLogout);
        imvAvatar = view.findViewById(R.id.imvAvatar);
        checkUser();
    }

    private void checkUser() {
        if(currentUser.getId() != -1){
            tvLogout.setText(getResources().getString(R.string.logout));
        }else {
            tvLogout.setText(getResources().getString(R.string.login));
        }
    }

    @Override
    public void onResume() {
        currentUser = DataLocalManager.getUser();
        checkUser();
        super.onResume();
    }
}