package com.example.course_mobile.service;

import com.example.course_mobile.data_local.DataLocalManager;
import com.example.course_mobile.model.category.Category;
import com.example.course_mobile.model.course.Course;
import com.example.course_mobile.model.lesson.Lesson;
import com.example.course_mobile.model.quiz.Quiz;
import com.example.course_mobile.model.user.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    //Link api https://fakestoreapi.com/products
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm::ss")
            .create();

    ApiService apiService = new Retrofit.Builder()
//            .baseUrl("http://10.0.3.2:8000/api/")
            .baseUrl("https://9ccf-2402-800-61b3-9975-2007-2af7-9d7d-f1cf.ngrok.io/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);
//    @GET("products")
//    Call<List<User>> listProduct();

    @GET("category")
    Call<List<Category>>listCategory();


    @GET("courses/")
    Call<List<Course>> listCourse(@Query("q") String query
            ,@Query("category_id") Integer categoryId);

    @GET("courses/{pk}/register/")
    Call<ResponseBody> checkRegister(@Header("Authorization") String token, @Path("pk") int id);

    @POST("courses/{pk}/register/")
    Call<ResponseBody> postRegister(@Header("Authorization") String token, @Path("pk") int id);

    @GET("courses/{pk}/lesson/")
    Call<List<Lesson>> getLesson(@Header("Authorization") String token, @Path("pk") int id);

    @GET("courses/{pk}/quiz/")
    Call<List<Quiz>> getQuiz(@Header("Authorization") String token, @Path("pk") int id);

    @FormUrlEncoded
    @POST("user/")
    Call<ResponseBody> userRegister(@Field("username") String userName
            , @Field("email") String email
            , @Field("password") String passWord);

    @FormUrlEncoded
    @POST("login/")
    Call<User> user(@Field("username") String userName, @Field("password") String passWord);
}

