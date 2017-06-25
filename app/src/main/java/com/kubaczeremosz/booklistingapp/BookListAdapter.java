package com.kubaczeremosz.booklistingapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class BookListAdapter extends ArrayAdapter<Book> {


    public BookListAdapter(Context context, List<Book> booksList) {
        super(context, 0, booksList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        final Book currentBook = getItem(position);
        TextView offerNameTextView = (TextView) convertView.findViewById(R.id.list_book_title);
        offerNameTextView.setText(currentBook.getTitle());

        TextView offerTextView = (TextView) convertView.findViewById(R.id.list_book_author);
        offerTextView.setText(currentBook.getAuthor());

//        ImageView imageView = (ImageView) convertView.findViewById(R.id.list_book_icon);
//        imageView.setImageResource(currentBook.getImageResourceId());

        ViewGroup item =(ViewGroup) convertView.findViewById(R.id.item);
        return convertView;
    }
}
