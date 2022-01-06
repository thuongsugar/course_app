package com.example.course_mobile.model.quiz;

import com.google.gson.annotations.SerializedName;

public class Quiz {
    private int id;
    private String name;
    @SerializedName("number_of_question")
    private int numberQuestion;

    public Quiz(int id, String name, int numberQuestion) {
        this.id = id;
        this.name = name;
        this.numberQuestion = numberQuestion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberQuestion() {
        return numberQuestion;
    }

    public void setNumberQuestion(int numberQuestion) {
        this.numberQuestion = numberQuestion;
    }
}
