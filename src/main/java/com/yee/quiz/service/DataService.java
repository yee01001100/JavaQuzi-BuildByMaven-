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

public class DataService {

    private static final String DATA_DIR = System.getProperty("user.dir") + File.separator + "data";
    private static final String usersJsonPath = DATA_DIR + File.separator + "users.json";
    private static final String questionsJsonPath = DATA_DIR + File.separator + "questions.json";
    private static final String quizLogJsonPath = DATA_DIR + File.separator + "quizLog.json";

    //加载users.json文件
    public static List<User> loadUsersFromJson(){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(usersJsonPath);

            if(!file.exists()){
                file.getParentFile().mkdirs();
                file.createNewFile();
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, new java.util.ArrayList<>());
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
                file.getParentFile().mkdirs();
                file.createNewFile();
                
                InputStream inputStream = Question.class.getClassLoader().getResourceAsStream("questions.json");
                if(inputStream != null){
                    List<Question> questions = objectMapper.readValue(inputStream, new TypeReference<List<Question>>(){});
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, questions);
                    inputStream.close();
                } else {
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, new java.util.ArrayList<>());
                }
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
                file.getParentFile().mkdirs();
                file.createNewFile();
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, new java.util.ArrayList<>());
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

    //新用户注册
    public static void registerUser(String username, String password){
        List<User> users = loadUsersFromJson();

        String newId = String.valueOf(users.size()+1);
        User newUser = new User(newId, "student", username, password);
        users.add(newUser);

        saveUsersToJson(users);
    }
}