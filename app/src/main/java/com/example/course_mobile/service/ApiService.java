package com.example.course_mobile.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ApiService {
    //Link api https://fakestoreapi.com/products
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm::ss")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://b7ac-2402-800-61b3-836d-cd0d-d1dc-9060-f3e1.ngrok.io/api/")
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
}

