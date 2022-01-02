package com.example.course_mobile.model.user;

import com.google.gson.annotations.SerializedName;

public class User {
    private String auth_token;
    @SerializedName("info_user")
    private DetailUser userDetail;

    public User(String auth_token, DetailUser userDetail) {
        this.auth_token = auth_token;
        this.userDetail = userDetail;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public DetailUser getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(DetailUser userDetail) {
        this.userDetail = userDetail;
    }

    @Override
    public String toString() {
        return "User{" +
                "auth_token='" + auth_token + '\'' +
                ", userDetail=" + userDetail +
                '}';
    }
}
