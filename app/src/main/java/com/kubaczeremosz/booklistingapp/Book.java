package com.kubaczeremosz.booklistingapp;

/**
 * Created by Kuba on 2017-06-10.
 */

public class Book {

    private String title;
    private String author;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }


}
