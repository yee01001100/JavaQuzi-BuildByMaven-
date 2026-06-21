package com.yee.quiz;

import java.util.List;
import java.util.Scanner;

import com.yee.quiz.Controller.PasswordUtil;
import com.yee.quiz.Controller.QuizController;
import com.yee.quiz.model.*;
import com.yee.quiz.service.*;

public class Main {
    public static void main(String[] args){
        while(true){
            QuizController quizController = new QuizController();
            quizController.run();
        }
    }
}