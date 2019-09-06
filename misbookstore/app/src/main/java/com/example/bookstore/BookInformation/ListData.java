package com.example.bookstore.BookInformation;

import android.net.Uri;

public class ListData {
//    private final String title;
//    private final int price;
//    private final String isbn;
//
    public String BookName;
    private String Price;
    private String ISBN;
    public String photoUrl;
    private String Author;
    private String Publisher;
    private String PublishDate;
    private String Version;
    private String Outline;
    private String Classification;
    private int Index;


    public ListData() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ListData(String BookName,String Price,String ISBN, String photoUrl,String Author, String Publisher,
                    String PublisherDate, String Version, String Outline, String Classification, int Index) {
        this.BookName = BookName;
        this.Price = Price;
        this.ISBN = ISBN;
        this.photoUrl = photoUrl;
        this.Author = Author;
        this.Publisher = Publisher;
        this.PublishDate = PublisherDate;
        this.Version = Version;
        this.Outline = Outline;
        this.Classification = Classification;
        this.Index = Index;
    }
    public String getTitle(){
        return BookName;
    }
    public String getPrice(){
        return Price;
    }
    public String getIsbn(){
        return ISBN;
    }
    public String getUrl(){return photoUrl;}
    public String getAuthor(){return Author;}
    public String getPublisher(){return Publisher;}
    public String getPublishDate(){return PublishDate;}
    public String getVersion(){return Version;}
    public String getOutline(){return Outline;}
    public String getClassification(){return Classification;}
    public int getIndex(){return Index;}
}
