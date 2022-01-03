package com.example.course_mobile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_lesson);
        getWindow().setStatusBarColor(Color.WHITE);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
//        ApiService.apiService.user("pxt","123")
//                .enqueue(new Callback<DetailUser>() {
//                    @Override
//                    public void onResponse(Call<DetailUser> call, Response<DetailUser> response) {
//                        Log.e("status",response.code() + "");
//                        DetailUser d =  response.body();
//                        Log.e("data",d.toString());
//                    }
//
//                    @Override
//                    public void onFailure(Call<DetailUser> call, Throwable t) {
//                        Log.e("Loi",t.toString());
//                    }
//                });
    }
}