package com.example.course_mobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final int REGISTER_DATA = 99;
    private static final int SUCCESS_REGISTER = 1;
    private static final int ERROR_REGISTER = 2;
    public static final String COURSE_DATA = "course data";
    private Button btnDetail;
    private ImageView imvDetail;
    private TextView tvTitle, tvDes,tvNumRegDetail;
    private ProgressBar pgBarDetail;
    private Animation animation;

    private boolean isRegistered;
    private Handler handler;

    private Course courseSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("UI Advance");

        initHandle();
        initUI();
        addEvents();


    }

    private void initHandle() {
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {

                switch (msg.what){
                    case REGISTER_DATA:
                        isRegistered = (boolean) msg.obj;
                        handleButton();
                        break;
                    case SUCCESS_REGISTER:

                        Toast.makeText(CourseDetailActivity.this,"dangki tahnh cong", Toast.LENGTH_LONG).show();
                        break;

                    case ERROR_REGISTER:
                        Toast.makeText(CourseDetailActivity.this,"dang ki that bai",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(CourseDetailActivity.this,LoginActivity.class));
                        finish();
                        break;
                    default:
                        break;
                }


                super.handleMessage(msg);
            }

        };
    }

    private void handleButton() {
        pgBarDetail.setVisibility(View.GONE);
        if (isRegistered){
            btnDetail.setText("Da dang ki, hoc ngay");
        }else {
            btnDetail.setText("Dang ki ngay");
        }
        btnDetail.setVisibility(View.VISIBLE);

    }

    private void addEvents() {
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClicked();
            }
        });
    }

    private void handleButtonClicked() {
        //kiem tra dang ki neu chua dki thi goi api dki,roi thi sang trang hoc
        if (isRegistered){
            Intent intentChoiceLesson = new Intent(CourseDetailActivity.this,ChoiceLessonActivity.class);
            intentChoiceLesson.putExtra(COURSE_DATA, courseSelected);
            startActivity(intentChoiceLesson);
            finish();
        }
        else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ApiService.apiService.postRegister(
                            DataLocalManager.getToken()
                            ,courseSelected.getId())
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    handleResponse(response);
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.e("Loi khong goi api",t.toString());
                                }
                            });
                }

                private void handleResponse(Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        Log.e("code tra ve",response.code() + "");
                        handler.sendEmptyMessage(SUCCESS_REGISTER);
                        return;
                    }else if(response.code() == 401){
                        handler.sendEmptyMessage(ERROR_REGISTER);
                    }
                }
            }).start();
        }
    }

    private void initUI() {
        pgBarDetail = findViewById(R.id.pgBarDetail);
        pgBarDetail.setVisibility(View.VISIBLE);
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
                            Message message = new Message();
                            message.what = REGISTER_DATA;
                            message.obj = isRegistered;
                            handler.sendMessage(message);
                    }catch (Exception e){
                        Log.e("Loi phan tich data",e.toString());
                    }
                }else if(response.code() == 401){

                    handler.sendEmptyMessage(ERROR_REGISTER);
                }

            }
        }).start();
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        courseSelected = (Course) bundle.get(SearchActivity.COURSE_OBJ);

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