package com.yee.quiz.service;

import com.yee.quiz.model.Question;
import com.yee.quiz.model.QuizConfig;
import com.yee.quiz.model.QuizLog;
import com.yee.quiz.model.QuizConfig;
import java.util.*;

public class QuizService {

    private List<Question> allQuestions;
    private List<Question> currentQuizQuestions;
    private int correctCount;
    private int wrongCount;
    private long quizStartTime;
    private int quizDurationMinutes;
    private boolean timeLimitEnabled;

    public QuizService() {
        this.allQuestions = DataService.loadQuestionsFromJson();
        this.currentQuizQuestions = new ArrayList<>();
        this.correctCount = 0;
        this.wrongCount = 0;
        this.quizStartTime = 0;
        this.quizDurationMinutes = 30;
        this.timeLimitEnabled = false;
    }

    public void startQuiz(int questionCount) {
        resetQuiz();
        
        if (questionCount > allQuestions.size()) {
            questionCount = allQuestions.size();
        }
        
        Collections.shuffle(allQuestions);
        currentQuizQuestions.addAll(allQuestions.subList(0, questionCount));
        
        QuizConfig config = DataService.loadQuizConfig();
        this.quizDurationMinutes = config.getDefaultDuration();
        this.timeLimitEnabled = true;
        this.quizStartTime = System.currentTimeMillis();
        
        System.out.println("\n=== 开始答题 ===");
        System.out.println("本次共 " + currentQuizQuestions.size() + " 道题，每题1分");
        System.out.println("考试时长：" + quizDurationMinutes + " 分钟\n");
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
            System.out.println("解析：" + question.getExplanation());
            System.out.println();
        }
        
        return isCorrect;
    }

    public QuizLog finishQuiz(String username) {
        long endTime = System.currentTimeMillis();
        long durationSeconds = (endTime - quizStartTime) / 1000;
        
        String timeStr = formatDuration(durationSeconds);
        
        int totalQuestions = correctCount + wrongCount;
        int score = (totalQuestions > 0) ? (int) Math.round((double) correctCount / totalQuestions * 100) : 0;
        
        List<QuizLog> quizLogs = DataService.loadQuizLogsFromJson();
        String logId = String.valueOf(quizLogs.size() + 1);
        
        QuizLog quizLog = new QuizLog(
            logId,
            username,
            String.valueOf(correctCount),
            String.valueOf(wrongCount),
            String.valueOf(score),
            timeStr
        );
        
        quizLogs.add(quizLog);
        DataService.saveQuizLogsToJson(quizLogs);
        
        return quizLog;
    }

    private String formatDuration(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        
        if (hours > 0) {
            return String.format("%d小时%d分%d秒", hours, minutes, secs);
        } else if (minutes > 0) {
            return String.format("%d分%d秒", minutes, secs);
        } else {
            return String.format("%d秒", secs);
        }
    }

    public long getElapsedTimeSeconds() {
        if (quizStartTime == 0) {
            return 0;
        }
        return (System.currentTimeMillis() - quizStartTime) / 1000;
    }

    public long getRemainingTimeSeconds() {
        if (!timeLimitEnabled || quizStartTime == 0) {
            return -1;
        }
        long elapsedSeconds = getElapsedTimeSeconds();
        long totalSeconds = quizDurationMinutes * 60L;
        return totalSeconds - elapsedSeconds;
    }

    public boolean isTimeUp() {
        return getRemainingTimeSeconds() <= 0;
    }

    public int getQuizDurationMinutes() {
        return quizDurationMinutes;
    }

    public void setQuizDurationMinutes(int minutes) {
        this.quizDurationMinutes = minutes;
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
        quizStartTime = 0;
        timeLimitEnabled = false;
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