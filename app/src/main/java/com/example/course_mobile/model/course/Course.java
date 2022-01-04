package com.example.course_mobile.model.course;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Course implements Serializable {
    private int id;
     private  String subject;
     @SerializedName("description")
     private String des;
     @SerializedName("total_user")
     private int registerNumber;
     private String image;

    public Course(int id, String subject, String des, int registerNumber, String image) {
        this.id = id;
        this.subject = subject;
        this.des = des;
        this.registerNumber = registerNumber;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(int registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
