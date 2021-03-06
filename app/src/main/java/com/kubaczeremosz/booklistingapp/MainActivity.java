package com.kubaczeremosz.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    private static final int BOOK_LOADER_ID = 1;

    private static String GOOGLE_BOOKS_REQUEST_URL;

    private BookListAdapter mAdapter;

    private TextView mText;

    private View loadingIndicator;

    private int searchNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //search view
        final EditText searchPhrase = (EditText) findViewById(R.id.queryfield);

        //listview
        mAdapter = new BookListAdapter(this, new ArrayList<Book>());
        ListView listView = (ListView) findViewById(R.id.list);

        //loading indicator
        loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        //textMessage
        mText = (TextView) findViewById(R.id.messageText);
        mText.setVisibility(View.GONE);

        //loadManager
        final LoaderManager loaderManager = getLoaderManager();
        if(searchNo>0){
            loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
        }

        //search button
        final Button findButton = (Button) findViewById(R.id.findButton);
        findButton.setText(R.string.find);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check connection
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    searchNo++;
                    mText.setVisibility(View.GONE);
                    loadingIndicator.setVisibility(View.VISIBLE);
                    String phrase = searchPhrase.getText().toString();
                    phrase = phrase.replaceAll(" ", "+");
                    GOOGLE_BOOKS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=" + phrase;
                    loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                }
                else {
                    mText.setVisibility(View.VISIBLE);
                    mText.setText(R.string.noConnection);
                    mAdapter.clear();
                }
            }
        });

        listView.setAdapter(mAdapter);

    }

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, GOOGLE_BOOKS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        if (searchNo > 0) {
            mText.setVisibility(View.VISIBLE);
        }
        if (mAdapter != null) {
            mAdapter.clear();
        }
        if (books != null && !books.isEmpty()) {
            mText.setText(R.string.results);
            mAdapter.addAll(books);
        }
        if (books != null && books.isEmpty()) {
            mText.setText(R.string.nothingfound);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        mAdapter.clear();
    }

}
