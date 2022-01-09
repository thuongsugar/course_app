package com.example.course_mobile.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.course_mobile.R;
import com.example.course_mobile.activity.ChoiceLessonActivity;
import com.example.course_mobile.activity.CourseDetailActivity;
import com.example.course_mobile.activity.LoginActivity;
import com.example.course_mobile.activity.QuizQuestionActivity;
import com.example.course_mobile.adapter.QuizAdapter;
import com.example.course_mobile.data_local.DataLocalManager;
import com.example.course_mobile.model.quiz.Quiz;
import com.example.course_mobile.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QuizFragment extends Fragment {
    private static final int QUIZ_DATA = 1;
    private static final int QUIZ_401 = 0;
    public static final String QUIZ_ID = "QUIZ_ID";

    private LinearLayout layoutNotQuiz;

    private RecyclerView rcvQuiz;
    private List<Quiz> quizList;
    private QuizAdapter quizAdapter;

    private ProgressBar pgQuizFragment;

    private int idCourse;
    private String imageCourse;
    private Handler handler;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments();
        if(bundle == null){
            idCourse = -1;
            imageCourse = "";
        }
        idCourse = bundle.getInt(ChoiceLessonActivity.COURSE_ID);
        imageCourse = bundle.getString(ChoiceLessonActivity.COURSE_IMAGE);
        initHandle();
    }

    private void initHandle() {
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case QUIZ_DATA:
                        quizList = (List<Quiz>) msg.obj;
                        if (quizList.size() == 0){
                            layoutNotQuiz.setVisibility(View.VISIBLE);
                            rcvQuiz.setVisibility(View.GONE);
                        }else {
                            rcvQuiz.setVisibility(View.VISIBLE);
                            quizAdapter.setQuizList(quizList,imageCourse);
                        }
                        pgQuizFragment.setVisibility(View.GONE);

                        break;
                    case QUIZ_401:
                        pgQuizFragment.setVisibility(View.GONE);
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        getActivity().finish();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);
        initUI(v);
        addEvents();
        return v;
    }

    private void addEvents() {
        quizAdapter.setOnClickQuiz(new QuizAdapter.OnClickQuiz() {
            @Override
            public void onClick(Quiz quiz) {
                openDialogQuiz(quiz);
            }
        });
    }

    private void openDialogQuiz(Quiz quiz) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_quiz);

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

        TextView tvTitle = dialog.findViewById(R.id.dialogTitle);
        TextView tvDes = dialog.findViewById(R.id.dialogDes);
        Button btnCancel = dialog.findViewById(R.id.btnDialogCancel);
        Button bntOk = dialog.findViewById(R.id.btnDialogOk);

        tvTitle.setText(quiz.getName());
        tvDes.setText(quiz.getNumberQuestion() +" " +getResources().getString(R.string.question));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bntOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), QuizQuestionActivity.class);
                intent.putExtra(QUIZ_ID,quiz.getId());
                startActivity(intent);
            }
        });

        dialog.show();
    }

    private void initUI(View v) {
        layoutNotQuiz = v.findViewById(R.id.layoutNotQuiz);
        layoutNotQuiz.setVisibility(View.GONE);
        pgQuizFragment = v.findViewById(R.id.pgQuizFragment);
        pgQuizFragment.setVisibility(View.VISIBLE);
        rcvQuiz = v.findViewById(R.id.rcvQuiz);
        quizAdapter = new QuizAdapter();
        quizList = new ArrayList<>();
        LinearLayoutManager l = new LinearLayoutManager(getContext()
                ,LinearLayoutManager.VERTICAL,false);
        rcvQuiz.setLayoutManager(l);
        rcvQuiz.setAdapter(quizAdapter);
        if (idCourse != -1){
            Log.e("idOurse", idCourse +"");
            callApiGetQuiz(idCourse);
        }

    }

    private void callApiGetQuiz(int idCourse) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiService.apiService.getQuiz(DataLocalManager.getToken(),idCourse)
                        .enqueue(new Callback<List<Quiz>>() {
                            @Override
                            public void onResponse(Call<List<Quiz>> call, Response<List<Quiz>> response) {
                                handleResponse(response);
                            }

                            @Override
                            public void onFailure(Call<List<Quiz>> call, Throwable t) {
                                pgQuizFragment.setVisibility(View.GONE);
                            }
                        });
            }

            private void handleResponse(Response<List<Quiz>> response) {
                if (response.isSuccessful()){
                    List<Quiz> quizList = response.body();
                    Message message = new Message();
                    message.what = QUIZ_DATA;
                    message.obj = quizList;
                    handler.sendMessage(message);
                }else if(response.code() == 401){
                    handler.sendEmptyMessage(QUIZ_401);
                }else {
                    pgQuizFragment.setVisibility(View.GONE);
                    Toast.makeText(getContext(),getResources().getString(R.string.error_server),Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }
}