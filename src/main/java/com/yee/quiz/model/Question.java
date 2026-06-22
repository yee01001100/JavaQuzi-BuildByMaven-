package com.yee.quiz.model;

public class Question {
    private String id;
    private String category;
    private String question;
    private String[] options;
    private String Answer;
    private String explanation;

    public Question(){}

    public Question(String id, String category, String question, String[] options, String Answer, String explanation){
        this.id = id;
        this.category = category;
        this.question = question;
        this.options = options;
        this.Answer = Answer;
        this.explanation = explanation;
    }
    // getter and setter
    public String getId(){return id;}
    public void setId(String id){this.id = id;}

    public String getCategory(){return category;}
    public void setCategory(String category){this.category = category;}

    public String getQuestion(){return question;}
    public void setQuestion(String question){this.question = question;}

    public String[] getOptions(){return options;}
    public void setOptions(String[] options){this.options = options;}

    public String getAnswer(){return Answer;}
    public void setAnswer(String answer){this.Answer = answer;}

    public String getExplanation(){return explanation;}
    public void setExplanation(String explanation){this.explanation = explanation;}
}