package com.example.bookstore.BookInformation;

public class ListData {
    private final String title;
    private final int price;
    private final String isbn;

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
