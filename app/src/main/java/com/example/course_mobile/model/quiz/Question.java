package com.example.course_mobile.model.quiz;

import java.util.List;

public class Question {
    private int id;
    private String content;
    private List<Answer> answer;

    public Question(int id, String content, List<Answer> answer) {
        this.id = id;
        this.content = content;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Answer> answer) {
        this.answer = answer;
    }
}
