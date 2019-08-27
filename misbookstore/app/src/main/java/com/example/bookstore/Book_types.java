package com.example.bookstore;

import java.util.HashMap;
import java.util.Map;

public class Book_types {
    private boolean search_tools;
    private boolean education;
    private boolean biography;
    private boolean kid;
    private boolean philosophy;
    private boolean travel;
    private boolean psychology;
    private boolean sociology;
    //public String test;

    public Map<String, Boolean> stars = new HashMap<>();


    public Book_types() {
        // Default constructor required for calls to DataSnapshot.getValue()
    }

    public Book_types(boolean search_tools, boolean education, boolean biography, boolean kid, boolean philosophy, boolean travel, boolean psychology, boolean sociology) {
        this.search_tools = search_tools;
        this.education = education;
        this.biography = biography;
        this.kid = kid;
        this.philosophy = philosophy;
        this.travel = travel;
        this.psychology = psychology;
        this.sociology = sociology;
        //this.test = test;
    }

    public boolean getSearchtools(){return search_tools;}
    public boolean getEducation(){return education;}
    public boolean getBiography(){return biography;}
    public boolean getKid(){return kid;}
    public boolean getPhilosophy(){return philosophy;}
    public boolean getTravel(){return travel;}
    public boolean getPsychology(){return psychology;}
    public boolean getSociology(){return sociology;}
    //public String forTest(){return test;}
}
