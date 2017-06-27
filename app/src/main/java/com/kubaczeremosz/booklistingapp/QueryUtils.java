package com.kubaczeremosz.booklistingapp;

import android.util.Log;
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


public class QueryUtils {

    public static ArrayList<Book> extractBook(String requestUrl) throws IOException {

        URL url = createUrl(requestUrl);
        String jasonResponse= makeHttpRequest(url);
        ArrayList<Book> books = new ArrayList<>();
        if(jasonResponse!=null){
            try {

                JSONObject root = new JSONObject(jasonResponse);
                JSONArray items= root.getJSONArray("items");
                for(int i=0; i<items.length();i++){
                    JSONObject currentBook=items.getJSONObject(i);
                    JSONObject properties = currentBook.getJSONObject("volumeInfo");
                    //String author = properties.getString("authors");
                    JSONArray authors = properties.getJSONArray("authors");
                    String author = authors.getString(0);
                    String title =properties.getString("title");
                    Book book =new Book(author,title);
                    books.add(book);
                }

            } catch (JSONException e) {
                Log.e("BookAsyncTask", "Problem parsing the books JSON results", e);
            }

            return books;
        }
        return null;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e("URL","Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
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
            Log.e("connection","Error with creating connecting", e);
            return null;

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }

        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
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

}
