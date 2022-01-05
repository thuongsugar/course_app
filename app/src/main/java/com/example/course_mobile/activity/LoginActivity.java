package com.example.course_mobile.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.course_mobile.R;
import com.example.course_mobile.data_local.DataLocalManager;
import com.example.course_mobile.model.user.User;
import com.example.course_mobile.service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ImageView imvShowPass;
    private ImageView imvHiddenPass;
    private EditText edtUserName;
    private EditText edtPass;
    private Button btnLogin;
    private TextView tvSignUp;
    private ProgressBar pgBarLogIn;
    private Toast toast;


    private ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent i = result.getData();
                        String userName = i.getStringExtra(SignUpActivity.USER_NAME_TAG);
                        edtUserName.setText(userName);
                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                if(!userName.isEmpty() && !passWord.isEmpty()){
                    //login call api
                    login(userName,passWord);
                    cancelDanger();
                    return;
                }else {
                    toast.makeText(LoginActivity.this, "Thong tin van con thieu",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void login(String userName, String passWord) {
        showProgressBar();
        ApiService.apiService.user(userName,passWord)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        handleLoginResponse(response);
                        hiddenProgressBar();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        hiddenProgressBar();
                        Log.e("loii",t.toString());
                    }
                });
    }

    private void handleLoginResponse(Response<User> response) {
        if(response.isSuccessful()){
            toast.makeText(LoginActivity.this,"Dang nhap thanh cong",Toast.LENGTH_SHORT).show();
            User user = response.body();
            DataLocalManager.setUser(user);
            DataLocalManager.setToken(user.getToken());

            // TODO: 03/01/2022 open home activity
            startActivity(new Intent(this,HomeActivity.class));
            finish();

        }
        else if(response.code() >= 500){
            toast.makeText(LoginActivity.this, "Loi server",Toast.LENGTH_SHORT).show();
        }
        else {
            toast.makeText(LoginActivity.this, "Thong tin dang nhap khong chinh xac",Toast.LENGTH_SHORT).show();
            dangerEditText();
        }
    }

    private void openSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intentActivityResultLauncher.launch(intent);
        
    }
    private void showProgressBar(){
        pgBarLogIn.setVisibility(View.VISIBLE);
    }
    private void hiddenProgressBar(){
        pgBarLogIn.setVisibility(View.GONE);
    }
    private void dangerEditText(){
        edtUserName.setBackground(getDrawable(R.drawable.border_edit_false));
        edtPass.setBackground(getDrawable(R.drawable.border_edit_false));
    }
    private void cancelDanger(){
        edtPass.setBackground(getDrawable(R.drawable.border_edit_text));
        edtUserName.setBackground(getDrawable(R.drawable.border_edit_text));

    }
    private void initUI() {
        imvHiddenPass = findViewById(R.id.imvHiddenPass);
        imvShowPass = findViewById(R.id.imvShowPass);
        edtUserName = findViewById(R.id.edtUserName);
        edtPass = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        pgBarLogIn = findViewById(R.id.pgBarLogin);

    }
}