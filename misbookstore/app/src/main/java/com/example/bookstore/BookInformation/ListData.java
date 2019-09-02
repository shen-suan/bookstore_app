package com.example.bookstore.BookInformation;

public class ListData {
//    private final String title;
//    private final int price;
//    private final String isbn;
//
    public String title;
    public int price;
    public String isbn;


    public ListData() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ListData(String title,int price,String isbn) {
        this.title = title;
        this.price = price;
        this.isbn = isbn;
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
}
