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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.course_mobile.R;
import com.example.course_mobile.activity.ChoiceLessonActivity;
import com.example.course_mobile.activity.LessonCourseActivity;
import com.example.course_mobile.activity.LoginActivity;
import com.example.course_mobile.adapter.LessonAdapter;
import com.example.course_mobile.adapter.ViewPagerLesson;
import com.example.course_mobile.data_local.DataLocalManager;
import com.example.course_mobile.model.lesson.Lesson;
import com.example.course_mobile.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LessonFragment extends Fragment {
    private static final int LESSON_DATA = 99;
    private static final int AUTHORIZE_LESSON = 0;
    private static final int ERROR_LESSON = 1;

    public static final String LESSON_CHOICE = "LESSON_CHOICE";

    private ProgressBar pgLessonFragment;
    private RecyclerView rcvLesson;
    private LessonAdapter lessonAdapter;
    private List<Lesson> lessonList;

    private Handler handler;
    private  int idCourse;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments();
        idCourse = getArguments().getInt(ChoiceLessonActivity.COURSE_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lesson, container, false);
        initHandle();
        initUI(view);
        addEvents();
        return view;
    }

    private void addEvents() {
        lessonAdapter.setOnClickLesson(new LessonAdapter.OnClickLesson() {
            @Override
            public void onClick(Lesson lesson) {
                openLessonCourse(lesson);
            }
        });
    }

    private void openLessonCourse(Lesson lesson) {
        Intent intent = new Intent(getContext(), LessonCourseActivity.class);
        intent.putExtra(LESSON_CHOICE,lesson);
        startActivity(intent);
    }

    private void initUI(View view) {
        pgLessonFragment = view.findViewById(R.id.pgLessonFragment);
        pgLessonFragment.setVisibility(View.VISIBLE);

        rcvLesson = view.findViewById(R.id.rcvLesson);
        lessonList = new ArrayList<>();
        lessonAdapter = new LessonAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()
                ,LinearLayoutManager.VERTICAL,false);
        rcvLesson.setLayoutManager(linearLayoutManager);
        rcvLesson.setAdapter(lessonAdapter);

        callApiGetLesson(idCourse);


    }
    private void callApiGetLesson(int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiService.apiService.getLesson(DataLocalManager.getToken(),id)
                        .enqueue(new Callback<List<Lesson>>() {
                            @Override
                            public void onResponse(Call<List<Lesson>> call
                                    , Response<List<Lesson>> response) {

                                handleResponse(response);
                                Log.e("data",response.code()+"");
                            }

                            @Override
                            public void onFailure(Call<List<Lesson>> call, Throwable t) {
                                pgLessonFragment.setVisibility(View.GONE);
                                Log.e("loi goi api lesson", t.toString());
                            }
                        });
            }

            private void handleResponse(Response<List<Lesson>> response) {
                if(response.isSuccessful()){
                    List<Lesson> lessonList = response.body();
                    Message message = new Message();
                    message.what = LESSON_DATA;
                    message.obj = lessonList;
                    handler.sendMessage(message);
                }
                else if(response.code() == 401){
                    handler.sendEmptyMessage(AUTHORIZE_LESSON);
                }else {
                    handler.sendEmptyMessage(ERROR_LESSON);
                }
            }
        }).start();
    }
    private void initHandle() {
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case LESSON_DATA:
                        lessonList = (List<Lesson>) msg.obj;
                        lessonAdapter.setLessonList(lessonList);
                        pgLessonFragment.setVisibility(View.GONE);
                        break;
                    case AUTHORIZE_LESSON:
                        pgLessonFragment.setVisibility(View.GONE);
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        getActivity().finish();
                        break;
                    case ERROR_LESSON:
                        Toast.makeText(getContext(),"Loi server",Toast.LENGTH_SHORT).show();
                        pgLessonFragment.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

}