package com.kubaczeremosz.booklistingapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /** Tag for the log messages */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //search for book
        final EditText searchPhrase = (EditText) findViewById(R.id.queryfield);

        //search button
        Button findButton = (Button) findViewById(R.id.findButton);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phrase = searchPhrase.getText().toString();
                phrase = phrase.replaceAll(" ","+");
                String GOOGLE_BOOKS_REQUEST_URL =
                        "https://www.googleapis.com/books/v1/volumes?q="+phrase;
                BookAsyncTask task = new BookAsyncTask(GOOGLE_BOOKS_REQUEST_URL);
                task.execute();
            }
        });


    }

    public void updateUI(ArrayList<Book> booksList){
        //list adapter
        BookListAdapter adapter = new BookListAdapter(this, booksList);

        //list view
        ListView listView=(ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    public void setMessage(String message){
        TextView text = (TextView) findViewById(R.id.messageText);
        text.setText(message);
    }

    public class BookAsyncTask extends AsyncTask<URL, Void, ArrayList<Book>> {

        String GOOGLE_BOOKS_REQUEST_URL;

        public BookAsyncTask(String url) {
            this.GOOGLE_BOOKS_REQUEST_URL=url;
        }

        @Override
        protected ArrayList<Book> doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(GOOGLE_BOOKS_REQUEST_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            ArrayList<Book> bookList = extractBook(jsonResponse);

            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return bookList;
        }

        @Override
        protected void onPostExecute(ArrayList<Book> bookList) {
            if (bookList == null) {
                setMessage(getString(R.string.nothingfound));
                return;
            }
            setMessage(getString(R.string.results));
            updateUI(bookList);
        }

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();

                if(urlConnection.getResponseCode()==200){
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }

            } catch (IOException e) {
                // TODO: Handle the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
                if (urlConnection == null){
                    setMessage(getString(R.string.noConnection));
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }


//    /**
//     * Return a list of {@link Book} objects that has been built up from
//     * parsing a JSON response.
//     */

        public ArrayList<Book> extractBook(String jasonResponse) {

            // Create an empty ArrayList that we can start adding earthquakes to
            ArrayList<Book> books = new ArrayList<>();

            // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
            // is formatted, a JSONException exception object will be thrown.
            // Catch the exception so the app doesn't crash, and print the error message to the logs.
            try {

                JSONObject root = new JSONObject(jasonResponse);
                JSONArray items= root.getJSONArray("items");
                for(int i=0; i<items.length();i++){
                    JSONObject currentBook=items.getJSONObject(i);
                    JSONObject properties = currentBook.getJSONObject("volumeInfo");
                    String author = properties.getString("authors");
                    String title =properties.getString("title");
                    Book book =new Book(author,title,R.drawable.icon);
                    books.add(book);
                }


            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("BookAsyncTask", "Problem parsing the earthquake JSON results", e);
            }

            // Return the list of earthquakes
            return books;
        }

    }
}
