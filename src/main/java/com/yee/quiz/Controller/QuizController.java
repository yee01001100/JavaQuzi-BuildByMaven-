package com.yee.quiz.Controller;

import java.util.List;
import java.util.Scanner;
import com.yee.quiz.model.*;
import com.yee.quiz.service.*;

public class QuizController{

    private Scanner scanner;
    private DataService dataService;
    private QuizService quizService;
    private User user;

    public QuizController(){
        this.scanner = new Scanner(System.in);
        this.dataService = new DataService();
        this.quizService = new QuizService();
    }

    public void StartMenu(){
        System.out.print(
                "##############################"+
                "\n#      Java知识在线测试系统     #"+
                "\n##############################\n"
        );
        System.out.print(
                "1.用户登录"+
                "\n2.用户注册"+
                "\n3.退出系统\n>"
        );
    }

    public void run(){
        while(true){
            StartMenu();
            String choice = scanner.nextLine().trim();

            switch (choice){
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleRegister();
                    break;
                case "3":
                    System.out.println("感谢使用！");
                    System.exit(0);
                    break;
                default:
                    System.out.println("无效的选择！请重新输入！");
            }
        }
    }

    private void handleLogin(){
        System.out.println("\n--- 用户登录 ---");
        System.out.print("请输入用户名："+"\n>");
        String username = scanner.nextLine().trim();

        System.out.print("请输入密码："+"\n>");
        String password = scanner.nextLine().trim();

        List<User> users = dataService.loadUsersFromJson();
        for(User user : users){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                System.out.println("登录成功！用户："+username+"\n");
                this.user = user;
                showQuizMenu();
                break;
            }else if(!DataService.isUsernameExist(username)){
                System.out.println("登录失败！用户不存在或密码错误！");
            }else{}
        }
    }

    private void handleRegister(){
        System.out.println("\n--- 用户注册 ---");
        System.out.print("请输入用户名："+"\n>");
        String username = scanner.nextLine().trim();

        if(dataService.isUsernameExist(username)){
            System.out.println("用户已存在！请输入其它用户名！");
            handleRegister();
            return;
        }

        System.out.print("请输入密码："+"\n>");
        String password = scanner.nextLine().trim();
        System.out.print("请再次确认密码："+"\n>");
        String confirmPassword = scanner.nextLine().trim();
        if(!password.equals(confirmPassword)){
            System.out.println("两次输入的密码不一致!");
            handleRegister();
            return;
        }

        dataService.registerUser(username, password);
        System.out.println("注册成功！请登录。用户："+username+"\n");
        handleLogin();
    }

    private void showQuizMenu(){
        while(true){
            System.out.print(
                    "##############################"+
                            "\n#            测试菜单          #"+
                            "\n##############################\n"
            );
            System.out.println("1.开始答题");
            System.out.println("2.查看历史成绩");
            System.out.println("3.返回上级菜单");
            System.out.print(">");
            String choice = scanner.nextLine().trim();
            switch(choice){
                case "1":
                    startQuiz();
                    break;
                case "2":
                    viewHistory();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("无效选择！");
            }
        }
    }

    private void startQuiz(){
        System.out.println("\n--- 开始答题 ---");
        System.out.print("请输入题目数量（最多100道）："+"\n>");
        String input = scanner.nextLine().trim();
        
        int questionCount;
        try {
            questionCount = Integer.parseInt(input);
            if (questionCount <= 0 || questionCount > 100) {
                System.out.println("题目数量必须在1-100之间！");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("输入无效，请输入数字！");
            return;
        }

        quizService.startQuiz(questionCount);
        
        while (quizService.hasNextQuestion()) {
            Question question = quizService.getNextQuestion();
            displayQuestion(question);
            
            System.out.print("请输入你的答案（A/B/C/D）："+"\n>");
            String userAnswer = scanner.nextLine().trim();
            
            if (userAnswer.isEmpty()) {
                System.out.println("答案不能为空！视为答错。\n");
                quizService.checkAnswer(question, "");
                continue;
            }
            
            quizService.checkAnswer(question, userAnswer);
        }
        
        QuizLog quizLog = quizService.finishQuiz(user.getUsername());
        showQuizResult(quizLog);
    }

    private void displayQuestion(Question question) {
        System.out.println("----------------------------------------");
        System.out.println("【" + question.getCategory() + "】" + question.getQuestion());
        
        String[] options = question.getOptions();
        for (String option : options) {
            System.out.println(option);
        }
        System.out.println();
    }

    private void showQuizResult(QuizLog quizLog) {
        System.out.println("\n========== 答题结果 ==========");
        System.out.println("用户名：" + quizLog.getName());
        System.out.println("答对题数：" + quizLog.getCorrectCount());
        System.out.println("答错题数：" + quizLog.getWrongCount());
        System.out.println("得分：" + quizLog.getScore() + " 分");
        System.out.println("==============================\n");
    }

    private void viewHistory() {
        System.out.println("\n--- 历史成绩 ---");
        
        List<QuizLog> history = quizService.getUserQuizHistory(user.getUsername());
        
        if (history.isEmpty()) {
            System.out.println("暂无答题记录！\n");
            return;
        }
        
        System.out.println("共 " + history.size() + " 条记录：\n");
        System.out.printf("%-5s %-10s %-8s %-8s %-6s%n", "序号", "用户名", "答对", "答错", "得分");
        System.out.println("--------------------------------------------");
        
        for (int i = 0; i < history.size(); i++) {
            QuizLog log = history.get(i);
            System.out.printf("%-5d %-10s %-8s %-8s %-6s%n",
                i + 1,
                log.getName(),
                log.getCorrectCount(),
                log.getWrongCount(),
                log.getScore() + "分"
            );
        }
        System.out.println();
    }
}