package com.example.course_mobile.fragment;

import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.course_mobile.R;
import com.example.course_mobile.activity.ChoiceLessonActivity;
import com.example.course_mobile.activity.CourseDetailActivity;
import com.example.course_mobile.activity.LoginActivity;
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
    private RecyclerView rcvQuiz;
    private List<Quiz> quizList;
    private QuizAdapter quizAdapter;

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
                        quizAdapter.setQuizList(quizList,imageCourse);
                        break;
                    case QUIZ_401:
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

    }

    private void initUI(View v) {
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
                    Toast.makeText(getContext(),"Loi server",Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }
}