package com.example.course_mobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.course_mobile.R;
import com.example.course_mobile.data_local.DataLocalManager;
import com.example.course_mobile.model.course.Course;
import com.example.course_mobile.service.ApiService;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetailActivity extends AppCompatActivity {
    private Button btnDetail;
    private ImageView imvDetail;
    private TextView tvTitle, tvDes,tvNumRegDetail;
    private Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("UI Advance");

        initUI();
        addEvents();


    }

    private void addEvents() {

    }

    private void initUI() {

        imvDetail = findViewById(R.id.imvCourseDetail);
        tvTitle = findViewById(R.id.tvTitleDetail);
        tvDes = findViewById(R.id.tvDesDetail);
        tvNumRegDetail = findViewById(R.id.tvNumRegisterDetail);

        btnDetail = findViewById(R.id.btnDetail);
        //add animation
        animation = new TranslateAnimation(0, 0, 10, -5);
        animation.setDuration(1000);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        btnDetail.setAnimation(animation);

        getBundle();



    }

    private void checkRegister(int id) {
        Log.e("token",id +"");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiService.apiService.checkRegister(DataLocalManager.getToken(),id)
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                handleRegisterResult(response);
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("Loi call api",t.toString());
                            }
                        });
            }

            private void handleRegisterResult(Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        boolean isRegistered = jsonObject.optBoolean("is_registered");
                        if(isRegistered){
                            btnDetail.setText("Da dang ky hoc ngay");
                        } else {
                            btnDetail.setText("Dang ky hoc");
                        }
                    }catch (Exception e){
                        Log.e("Loi phan tich data",e.toString());
                    }



                }
            }
        }).start();
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        Course courseSelected = (Course) bundle.get(SearchActivity.COURSE_OBJ);

        checkRegister(courseSelected.getId());

        Glide.with(this)
                .load(courseSelected.getImage())
                .centerCrop()
                .placeholder(R.drawable.course_defaut)
                .into(imvDetail);
        tvNumRegDetail.setText(courseSelected.getRegisterNumber() +"nguoi dang ki");
        tvTitle.setText(courseSelected.getSubject());
        tvDes.setText(courseSelected.getDes());

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}