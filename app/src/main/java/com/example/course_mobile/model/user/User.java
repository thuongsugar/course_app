package com.example.course_mobile.model.user;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("auth_token")
    private String token;
    private int id;
    @SerializedName("username")
    private String userName;
    private String email;
    private String image;

    public User(String token, int id, String userName, String email, String image) {
        this.token = token;
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.image = image;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "DetailUser{" +
                "auth_token='" + token + '\'' +
                ", id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
