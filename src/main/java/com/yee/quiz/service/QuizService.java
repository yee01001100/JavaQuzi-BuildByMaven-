package com.yee.quiz.service;

import com.yee.quiz.model.Question;
import com.yee.quiz.model.QuizLog;
import java.util.*;

public class QuizService {

    private List<Question> allQuestions;
    private List<Question> currentQuizQuestions;
    private int correctCount;
    private int wrongCount;

    public QuizService() {
        this.allQuestions = DataService.loadQuestionsFromJson();
        this.currentQuizQuestions = new ArrayList<>();
        this.correctCount = 0;
        this.wrongCount = 0;
    }

    public void startQuiz(int questionCount) {
        resetQuiz();
        
        if (questionCount > allQuestions.size()) {
            questionCount = allQuestions.size();
        }
        
        Collections.shuffle(allQuestions);
        currentQuizQuestions.addAll(allQuestions.subList(0, questionCount));
        
        System.out.println("\n=== 开始答题 ===");
        System.out.println("本次共 " + currentQuizQuestions.size() + " 道题，每题1分\n");
    }

    public boolean hasNextQuestion() {
        return !currentQuizQuestions.isEmpty();
    }

    public Question getNextQuestion() {
        if (currentQuizQuestions.isEmpty()) {
            return null;
        }
        return currentQuizQuestions.remove(0);
    }

    public boolean checkAnswer(Question question, String userAnswer) {
        String correctAnswer = question.getAnswer().trim().toUpperCase();
        String answer = userAnswer.trim().toUpperCase();
        
        boolean isCorrect = correctAnswer.equals(answer);
        
        if (isCorrect) {
            correctCount++;
            System.out.println("✓ 回答正确！\n");
        } else {
            wrongCount++;
            System.out.println("✗ 回答错误！正确答案是：" + question.getAnswer());
            System.out.println();
        }
        
        return isCorrect;
    }

    public QuizLog finishQuiz(String username) {
        int totalQuestions = correctCount + wrongCount;
        int score = (totalQuestions > 0) ? (int) Math.round((double) correctCount / totalQuestions * 100) : 0;
        
        List<QuizLog> quizLogs = DataService.loadQuizLogsFromJson();
        String logId = String.valueOf(quizLogs.size() + 1);
        
        QuizLog quizLog = new QuizLog(
            logId,
            username,
            String.valueOf(correctCount),
            String.valueOf(wrongCount),
            String.valueOf(score)
        );
        
        quizLogs.add(quizLog);
        DataService.saveQuizLogsToJson(quizLogs);
        
        return quizLog;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getWrongCount() {
        return wrongCount;
    }

    public int getTotalQuestions() {
        return correctCount + wrongCount;
    }

    private void resetQuiz() {
        currentQuizQuestions.clear();
        correctCount = 0;
        wrongCount = 0;
    }

    public List<QuizLog> getUserQuizHistory(String username) {
        List<QuizLog> allLogs = DataService.loadQuizLogsFromJson();
        List<QuizLog> userLogs = new ArrayList<>();
        
        for (QuizLog log : allLogs) {
            if (log.getName().equals(username)) {
                userLogs.add(log);
            }
        }
        
        return userLogs;
    }

    public List<QuizLog> getAllQuizLogs() {
        return DataService.loadQuizLogsFromJson();
    }
}