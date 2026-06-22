package com.yee.quiz.model;

public class QuizConfig {
    private int defaultDuration;
    private int defaultQuestionCount;

    public QuizConfig() {
        this.defaultDuration = 30;
        this.defaultQuestionCount = 10;
    }

    public QuizConfig(int defaultDuration, int defaultQuestionCount) {
        this.defaultDuration = defaultDuration;
    }

    public int getDefaultDuration() {
        return defaultDuration;
    }

    public void setDefaultDuration(int defaultDuration) {
        this.defaultDuration = defaultDuration;
    }

    public int getDefaultQuestionCount() {
        return defaultQuestionCount;
    }

    public void setDefaultQuestionCount(int defaultQuestionCount) {
        this.defaultQuestionCount = defaultQuestionCount;
    }
}
