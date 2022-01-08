package com.example.course_mobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.course_mobile.R;
import com.example.course_mobile.data_local.DataLocalManager;
import com.example.course_mobile.fragment.QuizFragment;
import com.example.course_mobile.model.quiz.Question;
import com.example.course_mobile.model.quiz.Quiz;
import com.example.course_mobile.service.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizQuestionActivity extends AppCompatActivity {
    private static final int QUESTION_DATA = 1;
    private static final int QUESTION_401 = 0;

    private TextView tvCurrentQuestion, tvQuestionContent,tvScore;
    private RadioGroup radioGroup;
    private RadioButton rb1,rb2,rb3,rb4;
    private Button btnNextQuestion;
    private ProgressBar pgQuestionQuiz;

    private int idQuiz;
    private int dangerColor;
    private int successColor;
    private int defaultColor;
    private Drawable defaultBorder;
    private Drawable dangerBorder;
    private Drawable successBorder;

    private int score;
    private boolean answer;
    private int totalQuestion;
    private int counterQuestion = 0;
    private List<Question> questionList;
    private Question currentQuestion;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_question);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        initHandle();
        initUI();
        addEvents();
    }

    private void initHandle() {
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == QUESTION_DATA){
                    questionList = (List<Question>) msg.obj;
                    totalQuestion = questionList.size();
                    renderQuestion();
                    pgQuestionQuiz.setVisibility(View.GONE);
                }
                else if (msg.what == QUESTION_401){
                    pgQuestionQuiz.setVisibility(View.GONE);
                    startActivity(new Intent(QuizQuestionActivity.this, LoginActivity.class));
                    finish();
                }
                super.handleMessage(msg);
            }
        };
    }

    private void renderQuestion() {
        answer = false;
        radioGroup.clearCheck();
        rb1.setTextColor(defaultColor);
        rb1.setBackground(defaultBorder);
        rb2.setTextColor(defaultColor);
        rb2.setBackground(defaultBorder);
        rb3.setTextColor(defaultColor);
        rb3.setBackground(defaultBorder);
        rb4.setTextColor(defaultColor);
        rb4.setBackground(defaultBorder);
        if(counterQuestion < totalQuestion){
            currentQuestion = questionList.get(counterQuestion);
            tvCurrentQuestion.setText(counterQuestion+1 +"/"+totalQuestion);
            tvQuestionContent.setText(currentQuestion.getContent());
            rb1.setText(currentQuestion.getAnswer().get(0).getContent());
            rb2.setText(currentQuestion.getAnswer().get(1).getContent());
            rb3.setText(currentQuestion.getAnswer().get(2).getContent());
            rb4.setText(currentQuestion.getAnswer().get(3).getContent());

            counterQuestion++;

        }else {
            openDialogQuiz();
        }
    }

    private void initUI() {
        pgQuestionQuiz = findViewById(R.id.pgQuestionQuiz);
        pgQuestionQuiz.setVisibility(View.VISIBLE);

        idQuiz = (int) getIntent().getExtras().get(QuizFragment.QUIZ_ID);
        dangerColor = ContextCompat.getColor(this, R.color.danger);
        successColor = ContextCompat.getColor(this, R.color.success);
        defaultColor = ContextCompat.getColor(this, R.color.dark);
        defaultBorder = getDrawable(R.drawable.bg_radio_button_defaute);
        dangerBorder = getDrawable(R.drawable.bg_radio_button_false);
        successBorder = getDrawable(R.drawable.bg_radio_button_true);

        tvCurrentQuestion = findViewById(R.id.tvCurrentQuestion);
        tvQuestionContent = findViewById(R.id.tvQuestionContent);
        tvScore = findViewById(R.id.tvScore);

        radioGroup = findViewById(R.id.radioGroup);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rb4 = findViewById(R.id.rb4);

        btnNextQuestion = findViewById(R.id.btnNextQuestion);

        callApiGetQuestion();
    }

    private void callApiGetQuestion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiService.apiService.getQuestion(DataLocalManager.getToken(),idQuiz)
                        .enqueue(new Callback<List<Question>>() {
                            @Override
                            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                                handleResponse(response);
                            }

                            @Override
                            public void onFailure(Call<List<Question>> call, Throwable t) {
                                Log.e("loi goi api question",t.toString());
                            }
                        });
            }

            private void handleResponse(Response<List<Question>> response) {
                if (response.isSuccessful()){
                    List<Question> questions = response.body();
                    Message message = new Message();
                    message.what = QUESTION_DATA;
                    message.obj = questions;
                    handler.sendMessage(message);
                }else if (response.code() == 401){
                    handler.sendEmptyMessage(QUESTION_401);
                }else {
                    pgQuestionQuiz.setVisibility(View.GONE);
                    Toast.makeText(QuizQuestionActivity.this, "Loi server"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }

    private void addEvents() {
        btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexChoice = radioGroup.indexOfChild(
                        findViewById(radioGroup.getCheckedRadioButtonId()));
                if(!answer){
                    if(indexChoice != -1){
                        checkAnswer();
                    }else {
                        Toast.makeText(QuizQuestionActivity.this
                                ,"Chon mot dap an",Toast.LENGTH_LONG).show();
                    }
                }else {
                    renderQuestion();
                }
            }
        });
    }

    private void checkAnswer() {
        answer = true;
        //dap an duoc chon
        RadioButton rbSelected = findViewById(radioGroup.getCheckedRadioButtonId());

        int indexSelectedAnswer = radioGroup.indexOfChild(rbSelected);

        //neu chon dung
        if(currentQuestion.getAnswer().get(indexSelectedAnswer).isCorrect()){
            score++;
            tvScore.setText("Diem: "+score);
        }else {//tim dap an
            for(int i =0; i<currentQuestion.getAnswer().size();i++){
                if (currentQuestion.getAnswer().get(i).isCorrect()){
                    indexSelectedAnswer = i;
                    break;
                }
            }
        }
        rb1.setTextColor(dangerColor);
        rb2.setTextColor(dangerColor);
        rb3.setTextColor(dangerColor);
        rb4.setTextColor(dangerColor);
        rb1.setBackground(dangerBorder);
        rb2.setBackground(dangerBorder);
        rb3.setBackground(dangerBorder);
        rb4.setBackground(dangerBorder);


        switch (indexSelectedAnswer){
            case 0:
                rb1.setTextColor(successColor);
                rb1.setBackground(successBorder);
                break;
            case 1:
                rb2.setTextColor(successColor);
                rb2.setBackground(successBorder);

                break;
            case 2:
                rb3.setTextColor(successColor);
                rb3.setBackground(successBorder);

                break;
            case 3:
                rb4.setTextColor(successColor);
                rb4.setBackground(successBorder);

                break;
        }

        if(counterQuestion < totalQuestion){
            btnNextQuestion.setText("Next");
        }else {
            btnNextQuestion.setText("Finish");
        }
    }
    private void openDialogQuiz() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_complete_quiz);

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

        TextView tvTitle = dialog.findViewById(R.id.dialogResultQuiz);
        Button bntComplete = dialog.findViewById(R.id.btnDialogComplete);

        tvTitle.setText(score + "/" +totalQuestion +" cau hoi");


        bntComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dialog.show();
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}