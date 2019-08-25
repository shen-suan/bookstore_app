package com.example.bookstore;


import java.util.HashMap;
import java.util.Map;

public class User {
    public String name;
    public String mail;
    public String userGender;
    public String birthday;
    public String books;


    public Map<String, Boolean> stars = new HashMap<>();


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String mail ,String userGender,  String birthday, String books) {
        this.name = name;
        this.mail = mail;
        this.userGender = userGender;
        this.birthday = birthday;
        this.books = books;
    }
    public String getName() {
        return name;
    }
    public String getMail() {
        return mail;
    }
    public String getUserGender(){ return userGender; }
    public String getbirthday(){
        return birthday;
    }
    public String getBooks(){ return books; }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("mail", mail);
        result.put("userGender", userGender);
        result.put("birthday", birthday);
        result.put("books", books);
        return result;
    }
}
