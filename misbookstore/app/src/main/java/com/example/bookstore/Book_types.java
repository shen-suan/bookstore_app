package com.example.bookstore;

import java.util.HashMap;
import java.util.Map;

public class Book_types {
    public boolean activity;
    public boolean travel;
    public boolean literature;
    public boolean language;
    public boolean reference_book;
    //public String test;

    public Map<String, Boolean> stars = new HashMap<>();


    public Book_types() {
        // Default constructor required for calls to DataSnapshot.getValue()
    }

    public Book_types(boolean activity, boolean travel, boolean literature, boolean language, boolean reference_book) {
        this.activity = activity;
        this.travel = travel;
        this.literature = literature;
        this.language = language;
        this.reference_book = reference_book;
        //this.test = test;
    }

    public boolean getActivity(){return activity;}
    public boolean getTravel(){return travel;}
    public boolean getLiterature(){return literature;}
    public boolean getLanguage(){return language;}
    public boolean getReference(){return reference_book;}
    //public String forTest(){return test;}
}
