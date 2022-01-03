package com.example.course_mobile.service;

import com.example.course_mobile.model.course.Course;
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
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    //Link api https://fakestoreapi.com/products
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm::ss")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://10.0.3.2:8000/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);
//    @GET("products")
//    Call<List<User>> listProduct();

//    @GET("category")
//    Call<List<Category>>listCategory();
//

    @GET("courses/")
    Call<List<Course>> listCourse(@Query("q") String query);

    @FormUrlEncoded
    @POST("user/")
    Call<ResponseBody> userRegister(@Field("username") String userName, @Field("email") String email, @Field("password") String passWord);

    @FormUrlEncoded
    @POST("login/")
    Call<User> user(@Field("username") String userName, @Field("password") String passWord);
}

