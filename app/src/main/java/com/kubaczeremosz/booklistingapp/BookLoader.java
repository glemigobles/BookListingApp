package com.kubaczeremosz.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.io.IOException;
import java.util.ArrayList;


public class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {

    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of books.
        ArrayList<Book> bookList = null;
        try {
            bookList = QueryUtils.extractBook(mUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookList;
    }
}
