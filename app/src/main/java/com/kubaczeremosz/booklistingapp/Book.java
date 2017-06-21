package com.kubaczeremosz.booklistingapp;

/**
 * Created by Kuba on 2017-06-10.
 */

public class Book {

    private String title;
    private String author;
    private int imageResourceId;


    public Book(String title, String author, int imageResourceId) {
        this.title = title;
        this.author = author;
        this.imageResourceId = imageResourceId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
