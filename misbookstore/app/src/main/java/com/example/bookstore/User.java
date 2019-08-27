package com.example.bookstore;


import java.util.HashMap;
import java.util.Map;

public class User {
    public String account;
    public String name;
    public String nickname;
    public String userGender;
    public String birthday;
    public String books;


    public Map<String, Boolean> stars = new HashMap<>();


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String account, String name, String nickname ,String userGender,  String birthday, String books) {
        this.account = account;
        this.name = name;
        this.nickname = nickname;
        this.userGender = userGender;
        this.birthday = birthday;
        this.books = books;
    }
    public String getAccount() { return account; }
    public String getName() {
        return name;
    }
    public String getNickname() {
        return nickname;
    }
    public String getUserGender(){ return userGender; }
    public String getbirthday(){
        return birthday;
    }
    public String getBooks(){ return books; }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("account", account);
        result.put("name", name);
        result.put("nickname", nickname);
        result.put("userGender", userGender);
        result.put("birthday", birthday);
        result.put("books", books);
        return result;
    }
}
