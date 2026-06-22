package com.yee.quiz.model;

public class QuizLog {
    private String id;
    private String name;
    private String correctCount;
    private String wrongCount;
    private String score;
    private String time;

    public QuizLog(){}

    public QuizLog(String id, String name, String correctCount, String wrongCount, String score, String time){
        this.id = id;
        this.name = name;
        this.correctCount = correctCount;
        this.wrongCount = wrongCount;
        this.score = score;
        this.time = time;
    }

    // getter and setter
    public String getId(){return id;}
    public void setId(String id){this.id = id;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getCorrectCount(){return correctCount;}
    public void setCorrectCount(String correctCount){this.correctCount = correctCount;}

    public String getWrongCount(){return wrongCount;}
    public void setWrongCount(String wrongCount){this.wrongCount = wrongCount;}

    public String getScore(){return score;}
    public void setScore(String score){this.score = score;}

    public String getTime(){return time;}
    public void setTime(String time){this.time = time;}
}
