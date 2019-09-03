package com.example.bookstore.BookInformation;

import android.net.Uri;

public class ListData {
//    private final String title;
//    private final int price;
//    private final String isbn;
//
    public String title;
    private int price;
    private String isbn;
    public String photoUrl;
    private String Author;
    private String Publisher;
    private String PublishDate;
    private String Version;
    private String Outline;
    private String Classification;


    public ListData() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ListData(String title,int price,String isbn, String photoUrl,String Author, String Publisher, String PublisherDate, String Version, String Outline, String Classification) {
        this.title = title;
        this.price = price;
        this.isbn = isbn;
        this.photoUrl = photoUrl;
        this.Author = Author;
        this.Publisher = Publisher;
        this.PublishDate = PublisherDate;
        this.Version = Version;
        this.Outline = Outline;
        this.Classification = Classification;
    }
    public String getTitle(){
        return title;
    }
    public int getPrice(){
        return price;
    }
    public String getIsbn(){
        return isbn;
    }
    public String getUrl(){return photoUrl;}
    public String getAuthor(){return Author;}
    public String getPublisher(){return Publisher;}
    public String getPublishDate(){return PublishDate;}
    public String getVersion(){return Version;}
    public String getOutline(){return Outline;}
    public String getClassification(){return Classification;}
}
