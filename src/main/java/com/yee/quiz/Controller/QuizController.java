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
        ArtTextPrinter.show();
        System.out.println("Github: https://github.com/yee01001100/JavaQuzi-BuildByMaven");
        System.out.println("Gitee: https://gitee.com/yee01001100/java-quzi-build-by-maven");
        System.out.println("Version: 1.0");
        System.out.print(
                "##############################"+
                "\n#      Java知识在线测试系统      #"+
                "\n##############################\n"
        );
        System.out.print(
                "1.用户登录"+
                "\n2.用户注册"+
                "\n3.退出系统\n"+
                "4.管理员登录\n> "
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
                case "4":
                    handleAdminLogin();
                    break;
                default:
                    System.out.println("无效的选择！请重新输入！");
            }
        }
    }

    private void handleLogin(){
        System.out.println("\n--- 用户登录 ---");
        System.out.print("请输入用户名："+"\n> ");
        String username = scanner.nextLine().trim();

        System.out.print("请输入密码："+"\n> ");
        String password = scanner.nextLine().trim();

        List<User> users = dataService.loadUsersFromJson();
        boolean loginSuccess = false;
        
        for(User user : users){
            if(user.getUsername().equals(username) && com.yee.quiz.Controller.PasswordUtil.verifyPassword(password, user.getPassword())){
                System.out.println("登录成功！用户："+username+"\n");
                System.out.println("Category: "+user.getCategory());
                this.user = user;
                loginSuccess = true;
                showQuizMenu();
                break;
            }
        }
        
        if(!loginSuccess){
            if(DataService.isUsernameExist(username)){
                System.out.println("登录失败！密码错误！");
            }else{
                System.out.println("登录失败！用户不存在！");
            }
        }
    }

    private void handleRegister(){
        System.out.println("\n--- 用户注册 ---");
        System.out.print("请输入用户名："+"\n> ");
        String username = scanner.nextLine().trim();

        if(dataService.isUsernameExist(username)){
            System.out.println("用户已存在！请输入其它用户名！");
            handleRegister();
            return;
        }

        System.out.print("请输入密码："+"\n> ");
        String password = scanner.nextLine().trim();
        System.out.print("请再次确认密码："+"\n> ");
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

    private void handleAdminLogin(){
        System.out.println("\n--- 管理员登录 ---");
        System.out.print("Please input the Administrator password:"+"\n");
        String password = scanner.nextLine().trim();
        List<User> users = DataService.loadUsersFromJson();
        if(com.yee.quiz.Controller.PasswordUtil.verifyPassword(password, users.get(0).getPassword())){
            this.user = users.get(0);
            System.out.println("登录成功！用户："+users.get(0).getUsername()+"\n");
            System.out.println("Category: "+users.get(0).getCategory());
            showAdminMenu();
        }else{
            System.out.println("登录失败！密码错误！");
        }
    }

    private void showAdminMenu(){
        while(true){
            System.out.println("\n--- 管理员菜单 ---");
            System.out.println("1.查看全部用户");
            System.out.println("2.查看全部答题记录");
            System.out.println("3.设置考试时长");
            System.out.println("4.设置题目数量");
            System.out.println("5.修改密码");
            System.out.println("6.返回上一级");
            System.out.print("> ");
            String choice = scanner.nextLine().trim();
            
            switch(choice){
                case "1":
                    viewAllUsers();
                    break;
                case "2":
                    viewAllQuizLogs();
                    break;
                case "3":
                    setQuizDuration();
                    break;
                case "4":
                    setQuestionCount();
                    break;
                case "5":
                    if (changePassword(user.getUsername())) {
                        return;
                    }
                    break;
                case "6":
                    return;
                default:
                    System.out.println("无效选择！");
            }
        }
    }

    private void setQuizDuration() {
        System.out.println("\n--- 设置考试时长 ---");
        QuizConfig config = DataService.loadQuizConfig();
        System.out.println("当前考试时长：" + config.getDefaultDuration() + " 分钟");
        System.out.print("请输入新的考试时长（分钟，5-180）：" + "\n> ");
        String input = scanner.nextLine().trim();
        
        try {
            int duration = Integer.parseInt(input);
            if (duration < 5 || duration > 180) {
                System.out.println("考试时长必须在5-180分钟之间！");
                return;
            }
            
            config.setDefaultDuration(duration);
            DataService.saveQuizConfig(config);
            System.out.println("考试时长已设置为 " + duration + " 分钟");
        } catch (NumberFormatException e) {
            System.out.println("输入无效，请输入数字！");
        }
    }

    private void setQuestionCount() {
        System.out.println("\n--- 设置题目数量 ---");
        QuizConfig config = DataService.loadQuizConfig();
        System.out.println("当前题目数量：" + config.getDefaultQuestionCount() + " 道");
        System.out.print("请输入新的题目数量（1-100）：" + "\n> ");
        String input = scanner.nextLine().trim();
        
        try {
            int count = Integer.parseInt(input);
            if (count < 1 || count > 100) {
                System.out.println("题目数量必须在1-100之间！");
                return;
            }
            
            List<Question> allQuestions = DataService.loadQuestionsFromJson();
            if (count > allQuestions.size()) {
                System.out.println("题目数量不能超过题库总数（" + allQuestions.size() + " 道）！");
                return;
            }
            
            config.setDefaultQuestionCount(count);
            DataService.saveQuizConfig(config);
            System.out.println("题目数量已设置为 " + count + " 道");
        } catch (NumberFormatException e) {
            System.out.println("输入无效，请输入数字！");
        }
    }

    private void viewAllUsers(){
        System.out.println("\n--- 全部用户列表 ---");
        
        List<User> users = dataService.loadUsersFromJson();
        
        if(users.isEmpty()){
            System.out.println("暂无用户记录！\n");
            return;
        }
        
        System.out.println("共 " + users.size() + " 个用户：\n");
        System.out.printf("%-5s %-15s %-15s%n", "ID", "用户名", "类别");
        System.out.println("---------------------------------------------");
        
        for(User user : users){
            System.out.printf("%-5s %-15s %-15s%n",
                user.getId(),
                user.getUsername(),
                user.getCategory()
            );
        }
        System.out.println();
    }

    private void viewAllQuizLogs(){
        System.out.println("\n--- 全部答题记录 ---");
        
        List<QuizLog> allLogs = quizService.getAllQuizLogs();
        
        if(allLogs.isEmpty()){
            System.out.println("暂无答题记录！\n");
            return;
        }
        
        System.out.println("共 " + allLogs.size() + " 条记录：\n");
        System.out.printf("%-5s %-10s %-8s %-8s %-6s %-15s%n", "序号", "用户名", "答对", "答错", "得分", "用时");
        System.out.println("--------------------------------------------------------------------");
        
        for(int i = 0; i < allLogs.size(); i++){
            QuizLog log = allLogs.get(i);
            System.out.printf("%-5d %-10s %-8s %-8s %-6s %-15s%n",
                i + 1,
                log.getName(),
                log.getCorrectCount(),
                log.getWrongCount(),
                log.getScore() + "分",
                log.getTime()
            );
        }
        System.out.println();
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
            System.out.println("3.修改密码");
            System.out.println("4.注销当前账户");
            System.out.println("5.返回上级菜单");
            System.out.print("> ");
            String choice = scanner.nextLine().trim();
            switch(choice){
                case "1":
                    startQuiz();
                    break;
                case "2":
                    viewHistory();
                    break;
                case "3":
                    if (changePassword(user.getUsername())) {
                        return;
                    }
                    break;
                case "4":
                    loggout(user.getUsername());
                    return;
                case "5":
                    return;
                default:
                    System.out.println("无效选择！");
            }
        }
    }

    private void startQuiz(){
        QuizConfig config = DataService.loadQuizConfig();
        int questionCount = config.getDefaultQuestionCount();
        
        System.out.println("\n--- 开始答题 ---");
        System.out.println("本次答题共 " + questionCount + " 道题");

        quizService.startQuiz(questionCount);
        
        int count = 1;
        long lastReminderTime = 0;
        boolean timeIsUp = false;
        
        while (quizService.hasNextQuestion()) {
            long remainingSeconds = quizService.getRemainingTimeSeconds();
            
            if (remainingSeconds <= 0) {
                System.out.println("\n⏰ 考试时间到！系统将自动提交试卷...");
                timeIsUp = true;
                break;
            }
            
            long elapsedMinutes = quizService.getElapsedTimeSeconds() / 60;
            if (elapsedMinutes > 0 && elapsedMinutes % 5 == 0 && elapsedMinutes != lastReminderTime) {
                System.out.println("\n剩余时间 " + (remainingSeconds / 60) + " 分钟");
                lastReminderTime = elapsedMinutes;
            }
            
            Question question = quizService.getNextQuestion();
            System.out.print(count + ".");
            count++;
            displayQuestion(question);
            
            System.out.println("【剩余时间：" + (remainingSeconds / 60) + " 分钟 " + (remainingSeconds % 60) + " 秒】");
            System.out.print("请输入你的答案（A/B/C/D）："+"\n> ");
            String userAnswer = scanner.nextLine().trim();
            
            if (userAnswer.isEmpty()) {
                System.out.println("答案不能为空！视为答错。\n");
                quizService.checkAnswer(question, "");
                continue;
            }
            
            quizService.checkAnswer(question, userAnswer);
        }
        
        if (!timeIsUp) {
            QuizLog quizLog = quizService.finishQuiz(user.getUsername());
            showQuizResult(quizLog);
        } else {
            QuizLog quizLog = quizService.finishQuiz(user.getUsername());
            System.out.println("\n========== 强制提交结果 ==========");
            System.out.println("用户名：" + quizLog.getName());
            System.out.println("答对题数：" + quizLog.getCorrectCount());
            System.out.println("答错题数：" + quizLog.getWrongCount());
            System.out.println("得分：" + quizLog.getScore() + " 分");
            System.out.println("用时：" + quizLog.getTime());
            System.out.println("（考试时间到，未完成的题目视为未作答）");
            System.out.println("==================================\n");
            System.out.println(("回车以继续..."));
            scanner.nextLine();
        }
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
        System.out.println("用时：" + quizLog.getTime());
        System.out.println("==============================\n");
        System.out.println(("回车以继续..."));
        scanner.nextLine();
    }

    private void viewHistory() {
        System.out.println("\n--- 历史成绩 ---");
        
        List<QuizLog> history = quizService.getUserQuizHistory(user.getUsername());
        
        if (history.isEmpty()) {
            System.out.println("暂无答题记录！\n");
            return;
        }
        
        System.out.println("共 " + history.size() + " 条记录：\n");
        System.out.printf("%-5s %-10s %-8s %-8s %-6s %-15s%n", "序号", "用户名", "答对", "答错", "得分", "用时");
        System.out.println("--------------------------------------------------------------------");
        
        for (int i = 0; i < history.size(); i++) {
            QuizLog log = history.get(i);
            System.out.printf("%-5d %-10s %-8s %-8s %-6s %-15s%n",
                i + 1,
                log.getName(),
                log.getCorrectCount(),
                log.getWrongCount(),
                log.getScore() + "分",
                log.getTime()
            );
        }
        System.out.println();
    }

    private void loggout(String name){
        System.out.println("你确定要注销当前账户(用户名：" + name + ")吗？");
        System.out.print("1.确定\n"+"2.取消"+"\n> ");
        String str = scanner.nextLine().trim();
        if(str.equals("1")){
            DataService.logoutUser(name);
            System.out.println("注销成功！");
            this.user = null;
            return;
        }else if(str.equals("2")){
            return;
        }else{
            System.out.println("无效输入！");
            loggout(name);
        }
    }

    private boolean changePassword(String username) {
        System.out.println("\n--- 修改密码 ---");
        
        System.out.print("请输入当前密码：" + "\n> ");
        String currentPassword = scanner.nextLine().trim();
        
        List<User> users = dataService.loadUsersFromJson();
        User currentUser = null;
        int userIndex = -1;
        
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                currentUser = users.get(i);
                userIndex = i;
                break;
            }
        }
        
        if (currentUser == null) {
            System.out.println("用户不存在！");
            return false;
        }
        
        if (!PasswordUtil.verifyPassword(currentPassword, currentUser.getPassword())) {
            System.out.println("当前密码错误！");
            return false;
        }
        
        System.out.print("请输入新密码：" + "\n> ");
        String newPassword = scanner.nextLine().trim();
        
        if (newPassword.isEmpty()) {
            System.out.println("密码不能为空！");
            return false;
        }
        
        System.out.print("请再次输入新密码：" + "\n> ");
        String confirmPassword = scanner.nextLine().trim();
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("两次输入的密码不一致！");
            return false;
        }
        
        String hashedPassword = PasswordUtil.hashPassword(newPassword);
        currentUser.setPassword(hashedPassword);
        users.set(userIndex, currentUser);
        
        DataService.saveUsersToJson(users);
        
        System.out.println("密码修改成功！请使用新密码重新登录。");
        System.out.println("（按回车键继续...）");
        scanner.nextLine();
        
        this.user = null;
        return true;
    }
}