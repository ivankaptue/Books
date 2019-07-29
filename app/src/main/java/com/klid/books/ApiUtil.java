package com.klid.books;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiUtil {

    private static final String QUERY_PARAMETER_KEY = "q";

    private ApiUtil() {}

    public static final String BASE_API_URL =
        "https://www.googleapis.com/books/v1/volumes";
    public static final String KEY = "KEY";
    public static final String API_KEY = "AIzaSyCdchFoKK9y1nwuQm4kKHOh_6go4KstkfA";

    public static URL buildUrl (String title) {
        URL url = null;
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
            .appendQueryParameter(QUERY_PARAMETER_KEY, title)
            .appendQueryParameter(KEY, API_KEY)
            .build();
        try {
            url = new URL(uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getJson(URL url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasData = scanner.hasNext();
            if (hasData) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            connection.disconnect();
        }

    }
}
