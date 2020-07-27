package com.example.examinations.Model;

public class Question {
    public String question,answer;

    public Question(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
    public Question()
    {

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
