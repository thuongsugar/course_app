package com.example.course_mobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.course_mobile.R;
import com.example.course_mobile.service.ApiService;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    public static final String USER_NAME_TAG = "userName";
    private ImageView imvShowPass;
    private ImageView imvHiddenPass;
    private EditText edtUserName;
    private EditText edtPass;
    private EditText edtEmail;
    private Button btnSignUp;
    private TextView tvLogin;
    private ProgressBar pgBarLogIn;
    private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //an actionbar
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

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String userName = edtUserName.getText().toString().trim();
               String email = edtEmail.getText().toString().trim();
               String passWord = edtPass.getText().toString().trim();

               if(userName.isEmpty() && email.isEmpty() && passWord.isEmpty()){
                   toast.makeText(SignUpActivity.this
                           ,getResources().getString(R.string.leak_data)
                           ,Toast.LENGTH_SHORT).show();
                   return;
               }
               if(userName.contains(" ")){
                   toast.makeText(SignUpActivity.this,getResources().getString(R.string.user_name_error)
                           ,Toast.LENGTH_SHORT).show();
                   return;
               }

                cancelDanger();
                userRegister(userName,email,passWord);

            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void userRegister(String userName, String email, String passWord) {
        showProgressBar();
        ApiService.apiService.userRegister(userName,email,passWord)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        handleResponse(response,userName);
                        hiddenProgressBar();

                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        hiddenProgressBar();
                        Log.e("loi",t.toString());
                    }
                });
    }

    private void handleResponse(Response<ResponseBody> response,String userName) {
        int status = response.code();
        if(response.isSuccessful()){
            toast.makeText(this,getResources().getString(R.string.success_register
            ),Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this,LoginActivity.class);
            intent.putExtra(USER_NAME_TAG, userName);
            setResult(RESULT_OK,intent);
            finish();
            return;
        }
        else if(status >= 500){
            toast.makeText(this,getResources().getString(R.string.error_server)
                    , Toast.LENGTH_SHORT).show();
        }
        else {

            try {
                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                String emailError = jsonObject.optString("email");
                String userNameError = jsonObject.optString("username");
                String passWordError = jsonObject.optString("password");
                dangerEditText(userNameError,emailError,passWordError);
//                Log.e("loi la name",userNameError);
//                Log.e("loi la email",emailError);
//                Log.e("loi la pass",passWordError);
            }
            catch (Exception e){
                Log.e("loi e",e.toString());
            }
        }


    }
    private void dangerEditText(String eUserName,String eEmail,String ePassWord){
        Animation animation = new TranslateAnimation(0, 0, 0, 10);
        animation.setDuration(9000);
        animation.setRepeatMode(Animation.RELATIVE_TO_SELF);
        animation.setRepeatCount(Animation.INFINITE);

        if (!eUserName.isEmpty()){
            edtUserName.setBackground(getDrawable(R.drawable.border_edit_false));
            edtUserName.setText("");
            edtUserName.setHint(eUserName);
        }
        if (!eEmail.isEmpty()){
            edtEmail.setBackground(getDrawable(R.drawable.border_edit_false));
            edtEmail.setText("");
            edtEmail.setHint(eEmail);

        }
        if (!ePassWord.isEmpty()){
            edtPass.setBackground(getDrawable(R.drawable.border_edit_false));
            edtPass.setText("");
            edtPass.setHint(ePassWord);

        }
    }

    private void cancelDanger(){
        edtPass.setBackground(getDrawable(R.drawable.border_edit_text));
        edtEmail.setBackground(getDrawable(R.drawable.border_edit_text));
        edtUserName.setBackground(getDrawable(R.drawable.border_edit_text));

    }

    private void showProgressBar(){
        pgBarLogIn.setVisibility(View.VISIBLE);
    }
    private void hiddenProgressBar(){
        pgBarLogIn.setVisibility(View.GONE);
    }

    private void initUI() {
        imvHiddenPass = findViewById(R.id.imvHiddenPassSignUp);
        imvShowPass = findViewById(R.id.imvShowPassSignUp);
        edtUserName = findViewById(R.id.edtUserNameSignUp);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPasswordSignUp);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);
        pgBarLogIn = findViewById(R.id.pgBarSignUp);


    }
}