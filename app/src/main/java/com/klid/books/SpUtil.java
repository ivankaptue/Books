package com.klid.books;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class SpUtil {

    public static final String PREF_NAME = "BooksPreferences";
    public static final String POSITION = "POSITION";
    public static final String QUERY = "QUERY";

    private SpUtil() {}

    public static SharedPreferences getPrefs (Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getPreferenceString(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }

    public static int getPreferenceInt(Context context, String key) {
        return getPrefs(context).getInt(key, 0);
    }

    public static void setPreferenceString(Context context, String key, String value) {
        SharedPreferences prefs = getPrefs(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setPreferenceInt(Context context, String key, int value) {
        SharedPreferences prefs = getPrefs(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static ArrayList<String> getQueryList(Context context) {
        ArrayList<String> queryList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String query = getPrefs(context).getString(QUERY + i, "");
            if (!query.isEmpty()) {
                query = query.replace(",", " ");
                queryList.add(query);
            }
        }
        return queryList;
    }
}
