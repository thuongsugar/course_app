package com.example.course_mobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.course_mobile.R;

public class LoginActivity extends AppCompatActivity {
    private ImageView imvShowPass;
    private ImageView imvHiddenPass;
    private EditText edtUserName;
    private EditText edtPass;
    private Button btnLogin;
    private TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getSupportActionBar().hide();
        initUI();
        addEvent();
    }

    private void addEvent() {
        imvShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                imvHiddenPass.setVisibility(View.VISIBLE);
                imvShowPass.setVisibility(View.GONE);
            }
        });

        imvHiddenPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                imvHiddenPass.setVisibility(View.GONE);
                imvShowPass.setVisibility(View.VISIBLE);
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edtUserName.getText().toString().trim();
                String passWord = edtPass.getText().toString().trim();
                if(!userName.isEmpty() || !passWord.isEmpty()){
                    //login call api

                    return;
                }else {
                    Toast.makeText(LoginActivity.this, "Thong tin van con thieu",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void openSignUpActivity() {
        Intent intent = new Intent();

    }

    private void initUI() {
        imvHiddenPass = findViewById(R.id.imvHiddenPass);
        imvShowPass = findViewById(R.id.imvShowPass);
        edtUserName = findViewById(R.id.edtUserName);
        edtPass = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

    }
}