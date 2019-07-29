package com.klid.books;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.net.URL;

public class BookListActivity extends AppCompatActivity {

    private ProgressBar mLoadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        mLoadingProgress = findViewById(R.id.pb_loading);
        try {
            URL url = ApiUtil.buildUrl("cooking");
            new BooksQueryTask().execute(url);
        } catch (Exception e) {
            Log.d("error" , e.getMessage());
        }

    }

    public class BooksQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            mLoadingProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String result = null;
            try {
                result = ApiUtil.getJson(searchUrl);
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            TextView tvResult = findViewById(R.id.tvResponse);
            TextView tvError = findViewById(R.id.tvError);
            if (result == null) {
                tvResult.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.VISIBLE);
            } else {
                tvResult.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.INVISIBLE);
            }
            tvResult.setText(result);
            mLoadingProgress.setVisibility(View.INVISIBLE);
        }

    }
}
