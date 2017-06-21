package com.kubaczeremosz.booklistingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //books list
        ArrayList<Book> booksList =new ArrayList<>();
        booksList.add(new Book("ksiazka","autor",R.drawable.icon));
        booksList.add(new Book("ksiazka2","autor",R.drawable.icon));
        booksList.add(new Book("ksiazka3","autor",R.drawable.icon));
        booksList.add(new Book("ksiazka4","autor",R.drawable.icon));

        //list adapter
        BookListAdapter adapter = new BookListAdapter(this,booksList);

        //list view
        ListView listView=(ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

    }
}
