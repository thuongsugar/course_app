package com.example.course_mobile.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.course_mobile.R;
import com.example.course_mobile.activity.LoginActivity;
import com.example.course_mobile.data_local.DataLocalManager;
import com.example.course_mobile.model.user.User;
import com.example.course_mobile.service.ApiService;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingFragment extends Fragment {
    private static final int SUCCESS_CHANGE_MAIL = 1;
    private static final int ERROR_CHANGE_MAIL = -1;
    private static final int ERROR_401 = 0;
    private static final int CHANGE_MAIL_400 = 2;
    private static final int CHANGE_PASS_400 = 3;
    private static final int ERROR_CHANGE_PASS = 4;
    private static final int SUCCESS_CHANGE_PASS = 5;


    private ConstraintLayout layoutEmail,layoutPassWord;
    private TextView tvUserName,tvEmail;
    private View view;
    private User currentUser;

    private Handler handler;
    private Dialog dialog;
    private EditText edtEmailChange;

    private Dialog dialogPassWord;


    @Override
    public void onResume() {
        currentUser = DataLocalManager.getUser();
        updateInfo();
        super.onResume();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        initHandle();
        initUI();
        addEvents();
        return view;
    }

    private void initHandle() {
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case SUCCESS_CHANGE_MAIL:
                        currentUser = (User) msg.obj;
                        DataLocalManager.setUser(currentUser);
                        updateInfo();
                        dialog.dismiss();
                        break;

                    case ERROR_401:
                        dialog.dismiss();
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        break;

                    case CHANGE_MAIL_400:

                    case CHANGE_PASS_400:
                        String emailError = (String) msg.obj;
                        Toast toast = Toast.makeText(getContext()
                                ,emailError,Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,-15);
                        toast.show();
                        break;

                    case ERROR_CHANGE_MAIL:
                        dialog.dismiss();
                        Toast toast1 =  Toast.makeText(getContext(),getResources().getString(R.string.error_server)
                                ,Toast.LENGTH_SHORT);
                        toast1.setGravity(Gravity.CENTER,0,-15);
                        toast1.show();
                        break;

                    case SUCCESS_CHANGE_PASS:
                        Toast toast2 = Toast.makeText(getContext()
                                ,getResources().getString(R.string.success_change_pass)
                                ,Toast.LENGTH_SHORT);
                        toast2.setGravity(Gravity.CENTER,0,-15);
                        toast2.show();
                        dialogPassWord.dismiss();
                        break;

                    case ERROR_CHANGE_PASS:
                        dialogPassWord.dismiss();
                        Toast toast3 =  Toast.makeText(getContext(),getResources().getString(R.string.error_server)
                                ,Toast.LENGTH_SHORT);
                        toast3.setGravity(Gravity.CENTER,0,-15);
                        toast3.show();
                        break;
                }


                super.handleMessage(msg);
            }
        };
    }

    private void addEvents() {
        layoutEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogChangeEmail();
            }
        });

        layoutPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogChangePassWord();
            }
        });
    }

    private void openDialogChangePassWord() {
        dialogPassWord = new Dialog(getContext());
        dialogPassWord.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPassWord.setContentView(R.layout.dialog_change_pass);

        Window window = dialogPassWord.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        dialogPassWord.setCancelable(false);

        EditText edtOldPass = dialogPassWord.findViewById(R.id.dialogEdtOldPass);
        EditText edtPass = dialogPassWord.findViewById(R.id.dialogEdtPass);
        EditText edtPass1 = dialogPassWord.findViewById(R.id.dialogEdtPass1);

        Button btnCancel = dialogPassWord.findViewById(R.id.btnChangePassCancel);
        Button bntOk = dialogPassWord.findViewById(R.id.btnChangePassOk);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPassWord.dismiss();
            }
        });
        bntOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = edtPass.getText().toString();
                String pass1 = edtPass1.getText().toString();
                String oldPass = edtOldPass.getText().toString();
                if( !pass.equals(pass1)){
                    Toast toast = Toast.makeText(getContext()
                            ,getResources().getString(R.string.pass_no_duplicate)
                            ,Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,-15);
                    toast.show();
                    return;
                }
                if(oldPass.isEmpty() || pass.isEmpty()){
                    Toast toast = Toast.makeText(getContext()
                            ,getResources().getString(R.string.leak_data)
                            ,Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,-15);
                    toast.show();
                    return;
                }

                callApiChangePass(oldPass,pass,pass1);
            }
        });

        dialogPassWord.show();
    }

    private void callApiChangePass(String oldPass, String pass, String pass1) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiService.apiService.updateUserPassword(DataLocalManager.getToken()
                        ,currentUser.getId()
                        ,pass
                        , pass1
                        , oldPass)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        handleResponse(response);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("loi goi api change pass",t.toString());
                    }
                });
            }

            private void handleResponse(Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    handler.sendEmptyMessage(SUCCESS_CHANGE_PASS);
                }else if (response.code() == 401){
                    handler.sendEmptyMessage(ERROR_401);
                }
                else if (response.code() == 400) {
                    String oldPassError = "";
                    String passError = "";
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        oldPassError = jsonObject.optString("old_password");
                        passError = jsonObject.optString("password");
                        Message message = new Message();
                        message.what = CHANGE_PASS_400;
                        if ( !oldPassError.isEmpty() ){
                            message.obj = oldPassError;
                        }else {
                            message.obj = passError;
                        }
                        handler.sendMessage(message);
                    }catch (Exception e){
                    }
                }else {
                    handler.sendEmptyMessage(ERROR_CHANGE_PASS);
                }
            }
        }).start();
    }

    private void openDialogChangeEmail() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_email);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        dialog.setCancelable(false);

        edtEmailChange = dialog.findViewById(R.id.dialogEdtEmail);
        Button btnCancel = dialog.findViewById(R.id.btnChangeEmailCancel);
        Button bntOk = dialog.findViewById(R.id.btnChangeEmailOk);

        edtEmailChange.setText(currentUser.getEmail());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bntOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailNew = edtEmailChange.getText().toString().trim();
                if (emailNew.isEmpty()){
                    Toast toast = Toast.makeText(getContext(),getResources().getString(R.string.leak_data)
                            ,Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,15);
                    toast.show();
                    return;
                }
                callApiUpdateUser(emailNew);
            }
        });

        dialog.show();
    }

    private void callApiUpdateUser(String emailNew) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiService.apiService.updateUserInfo(DataLocalManager.getToken()
                                                , currentUser.getId(), emailNew)
                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                handleResponse(response);
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });
            }

            private void handleResponse(Response<User> response) {
                if (response.isSuccessful()){
                    User userNew = response.body();
                    Message message = new Message();
                    message.what = SUCCESS_CHANGE_MAIL;
                    message.obj = userNew;
                    handler.sendMessage(message);

                }else if(response.code() == 401){
                    handler.sendEmptyMessage(ERROR_401);
                }else if(response.code() == 400){
                    String emailError = "";
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        emailError = jsonObject.optString("email");
                        Message message = new Message();
                        message.what = CHANGE_MAIL_400;
                        message.obj = emailError;
                        handler.sendMessage(message);
                    }catch (Exception e){

                    }
                }
                else {
                    handler.sendEmptyMessage(ERROR_CHANGE_MAIL);
                }
            }
        }).start();
    }


    private void initUI() {
        currentUser = DataLocalManager.getUser();

        layoutEmail = view.findViewById(R.id.layoutSettingEmail);
        layoutPassWord = view.findViewById(R.id.layoutSettingPass);

        tvEmail = view.findViewById(R.id.tvEmailSetting);
        tvUserName = view.findViewById(R.id.tvUserNameSetting);

        updateInfo();
    }

    private void updateInfo() {
        tvUserName.setText(currentUser.getUserName());
        tvEmail.setText(currentUser.getEmail());
    }
}