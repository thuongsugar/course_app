package com.example.course_mobile.service;

import com.example.course_mobile.model.user.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    //Link api https://fakestoreapi.com/products
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm::ss")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://cb63-2402-800-61b3-836d-3cd9-e096-11f4-611d.ngrok.io/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);
//    @GET("products")
//    Call<List<User>> listProduct();

//    @GET("category")
//    Call<List<Category>>listCategory();
//
//    @GET("courses")
//    Call<CourseList> listCourse();
    @FormUrlEncoded
    @POST("login/")
    Call<User> user(@Field("username") String userName, @Field("password") String passWord);
}

