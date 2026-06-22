package com.yee.quiz.service;

import java.util.List;
import java.io.InputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.yee.quiz.model.*;
import com.yee.quiz.Controller.PasswordUtil;

public class DataService {

    private static final String DATA_DIR = System.getProperty("user.dir") + File.separator + "data";
    private static final String usersJsonPath = DATA_DIR + File.separator + "users.json";
    private static final String questionsJsonPath = DATA_DIR + File.separator + "questions.json";
    private static final String quizLogJsonPath = DATA_DIR + File.separator + "quizLog.json";
    private static final String quizConfigJsonPath = DATA_DIR + File.separator + "quizConfig.json";

    //加载users.json文件
    public static List<User> loadUsersFromJson(){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(usersJsonPath);

            if(!file.exists()){
                initializeDataFile("users.json", file);
            }

            List<User> users = objectMapper.readValue(file, new TypeReference<List<User>>(){});
            return users;
        }catch (Exception e){
            throw new RuntimeException("读取users.json失败："+e.getMessage(),e);
        }
    }

    //加载questions.json文件
    public static List<Question> loadQuestionsFromJson(){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(questionsJsonPath);

            if(!file.exists()){
                initializeDataFile("questions.json", file);
            }

            List<Question> questions = objectMapper.readValue(file, new TypeReference<List<Question>>(){});
            return questions;
        }catch (Exception e){
            throw new RuntimeException("读取questions.json失败："+e.getMessage(),e);
        }
    }

    //加载quizLog.json文件
    public static List<QuizLog> loadQuizLogsFromJson(){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(quizLogJsonPath);

            if(!file.exists()){
                initializeDataFile("quizLog.json", file);
            }

            List<QuizLog> quizLogs = objectMapper.readValue(file, new TypeReference<List<QuizLog>>(){});
            return quizLogs;
        }catch (Exception e){
            throw new RuntimeException("读取quizLog.json失败："+e.getMessage(),e);
        }
    }

    //判断用户是否存在
    public static boolean isUsernameExist(String username){
        for(User user : loadUsersFromJson()){
            if(user.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    private static void initializeDataFile(String resourceName, File targetFile) throws IOException {
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();
        
        InputStream inputStream = DataService.class.getClassLoader().getResourceAsStream(resourceName);
        ObjectMapper objectMapper = new ObjectMapper();
        
        if(inputStream != null){
            List<?> data = objectMapper.readValue(inputStream, new TypeReference<List<?>>(){});
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(targetFile, data);
            inputStream.close();
        } else {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(targetFile, new java.util.ArrayList<>());
        }
    }

    //保存新用户至users.json
    public static void saveUsersToJson(List<User> users){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(usersJsonPath);

            if(!file.exists()){
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            
            FileWriter fileWriter = new FileWriter(file);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(fileWriter, users);
            fileWriter.close();
        }catch (IOException e){
            throw new RuntimeException("保存users.json失败："+e.getMessage(),e);
        }
    }

    //保存答题日志至quizLog.json
    public static void saveQuizLogsToJson(List<QuizLog> quizLogs){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(quizLogJsonPath);

            if(!file.exists()){
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            
            FileWriter fileWriter = new FileWriter(file);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(fileWriter, quizLogs);
            fileWriter.close();
        }catch (IOException e){
            throw new RuntimeException("保存quizLog.json失败："+e.getMessage(),e);
        }
    }

    //保存问题至questions.json
    public static void saveQuestionsToJson(List<Question> questions){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(questionsJsonPath);

            if(!file.exists()){
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(fileWriter, questions);
            fileWriter.close();
        }catch (IOException e){
            throw new RuntimeException("保存questions.json失败："+e.getMessage(),e);
        }
    }

    //新用户注册
    public static void registerUser(String username, String password){
        List<User> users = loadUsersFromJson();

        String newId = String.valueOf(users.size()+1);
        String hashedPassword = PasswordUtil.hashPassword(password);
        User newUser = new User(newId, "student", username, hashedPassword);
        users.add(newUser);

        saveUsersToJson(users);
    }

    //用户注销
    public static void logoutUser(String username){
        List<User> users = loadUsersFromJson();
        for(int i=0;i<users.size();i++){
            User user = users.get(i);
            if(user.getUsername().equals(username)){
                users.remove(i);
                break;
            }
        }
        saveUsersToJson(users);
    }

    public static QuizConfig loadQuizConfig() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(quizConfigJsonPath);

            if (!file.exists()) {
                initializeConfigFile(file);
            }

            QuizConfig config = objectMapper.readValue(file, QuizConfig.class);
            return config;
        } catch (Exception e) {
            throw new RuntimeException("读取quizConfig.json失败：" + e.getMessage(), e);
        }
    }

    public static void saveQuizConfig(QuizConfig config) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(quizConfigJsonPath);

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(fileWriter, config);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("保存quizConfig.json失败：" + e.getMessage(), e);
        }
    }

    private static void initializeConfigFile(File targetFile) throws IOException {
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();
        
        ObjectMapper objectMapper = new ObjectMapper();
        QuizConfig defaultConfig = new QuizConfig(30, 10);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(targetFile, defaultConfig);
    }
}