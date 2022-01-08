package com.example.course_mobile.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.course_mobile.R;
import com.example.course_mobile.activity.CourseDetailActivity;
import com.example.course_mobile.activity.SearchActivity;
import com.example.course_mobile.adapter.CategoryAdapter;
import com.example.course_mobile.adapter.CourseAdapter;
import com.example.course_mobile.data_local.DataLocalManager;
import com.example.course_mobile.model.category.Category;
import com.example.course_mobile.model.course.Course;
import com.example.course_mobile.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CourseFragment extends Fragment {
    private static final int CATEGORY_LIST = 0;
    private static final int COURSE_LIST = 1;
    public static final String TAG_CATEGORY = "idCategory";

    private RecyclerView rcvCourseRegistered;
    private List<Course> courseList;
    private CourseAdapter courseAdapter;

    private RecyclerView rcvCategory;
    private List<Category> categoryList;
    private CategoryAdapter categoryAdapter;

    private TextView tvUserName;
    private SwipeRefreshLayout rfCourse;
    private EditText edtSearch;
    private ProgressBar pgHome;
    private LinearLayout layoutNotFoundFragmentCourse;
    private String searchContent;

    private  Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("goi lai","oncreate");
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e("goi  lai","actack");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        initUI(view);
        initHandle();
        addEvents();
        return view;
    }

    private void initHandle() {
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.e("data chuyen ve",msg.what + "");
                if(msg.what == CATEGORY_LIST){
                    categoryList = (List<Category>) msg.obj;
                    categoryAdapter.setCategoryList(categoryList);
                }
                else if(msg.what == COURSE_LIST){
                    courseList = (List<Course>) msg.obj;
                    courseAdapter.setDataCourses(courseList);
                    pgHome.setVisibility(View.GONE);
                    if (courseList.size() == 0){
                        layoutNotFoundFragmentCourse.setVisibility(View.VISIBLE);
                    }

                }
                super.handleMessage(msg);
            }
        };


    }

    private void addEvents() {
        categoryAdapter.onClickCategory(new CategoryAdapter.OnClickCategory() {
            @Override
            public void onClick(Category categoryClicked) {
                // TODO: 05/01/2022 xuly click danh muc
                Intent intentResultSearch = new Intent(getContext(), SearchActivity.class);
                intentResultSearch.putExtra(TAG_CATEGORY,categoryClicked);
                startActivity(intentResultSearch);
            }
        });

        rfCourse.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callApiCourse(searchContent);
            }
        });

        edtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)){

                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);

                   searchContent = edtSearch.getText().toString().trim();
                    callApiCourse(searchContent);
                    return true;
                }
                return false;
            }
        });

        courseAdapter.onClickCourse(new CourseAdapter.OnClickCourse() {
            @Override
            public void onClick(Course courseClicked) {
                openDetailActivity(courseClicked);
            }
        });
    }

    private void openDetailActivity(Course courseClicked) {
        Intent intentCourseDetail = new Intent(getContext(), CourseDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SearchActivity.COURSE_OBJ,courseClicked);
        intentCourseDetail.putExtras(bundle);
        startActivity(intentCourseDetail);
    }

    @SuppressLint("ResourceAsColor")
    private void initUI(View view) {
        layoutNotFoundFragmentCourse = view.findViewById(R.id.layoutNotFoundFragmentCourse);
        layoutNotFoundFragmentCourse.setVisibility(View.GONE);

        rcvCategory = view.findViewById(R.id.rcvCategory);
        categoryAdapter = new CategoryAdapter();
        categoryList = new ArrayList<>();

        tvUserName = view.findViewById(R.id.tvUserNameHome);
        tvUserName.setText(DataLocalManager.getUser().getUserName());
        rfCourse = view.findViewById(R.id.rfCourse);
        rfCourse.setColorSchemeColors(getResources().getColor(R.color.primary));
        pgHome = view.findViewById(R.id.pgHome);
        pgHome.setVisibility(View.VISIBLE);
        edtSearch = view.findViewById(R.id.edtSearch);
        searchContent = edtSearch.getText().toString().trim();

        rcvCourseRegistered = view.findViewById(R.id.rcvCourseRegistered);
        courseAdapter = new CourseAdapter();
        courseList = new ArrayList<>();

        LinearLayoutManager linearLayoutManagerCourse = new LinearLayoutManager(getContext()
                ,LinearLayoutManager.VERTICAL
                ,false);
        rcvCourseRegistered.setLayoutManager(linearLayoutManagerCourse);
        rcvCourseRegistered.setAdapter(courseAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()
                ,LinearLayoutManager.HORIZONTAL
                ,false);
        rcvCategory.setLayoutManager(linearLayoutManager);
        rcvCategory.setAdapter(categoryAdapter);
        callApiCategory();
        callApiCourse(searchContent);
    }

    private void callApiCourse(String queryCourse) {
        pgHome.setVisibility(View.VISIBLE);
        if(queryCourse.isEmpty()){
            queryCourse = null;
        }
        String finalQueryCourse = queryCourse;
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiService.apiService.listCourse(finalQueryCourse,null)
                        .enqueue(new Callback<List<Course>>() {
                            @Override
                            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                                rfCourse.setRefreshing(false);
                                if(response.isSuccessful()){
                                    List<Course> courseList = response.body();
                                    Message message = new Message();
                                    message.what = COURSE_LIST;
                                    message.obj = courseList;
                                    handler.sendMessage(message);
                                    return;
                                }
                                pgHome.setVisibility(View.GONE);
                                Log.e("Status", response.code()+"");
                            }

                            @Override
                            public void onFailure(Call<List<Course>> call, Throwable t) {
                                rfCourse.setRefreshing(false);
                                pgHome.setVisibility(View.GONE);


                            }});
            }
        }).start();
    }

    private void callApiCategory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiService.apiService.listCategory()
                        .enqueue(new Callback<List<Category>>() {
                            @Override
                            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                                if (response.isSuccessful()){
                                    List<Category> categoryList = response.body();
                                    Message message = new Message();
                                    message.what = CATEGORY_LIST;
                                    message.obj = categoryList;
                                    handler.sendMessage(message);
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Category>> call, Throwable t) {
                                Log.e("loi","loi k goi api category");
                            }
                        });


            }
        }).start();
    }
}