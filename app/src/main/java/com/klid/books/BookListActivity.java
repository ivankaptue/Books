package com.klid.books;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;
import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ProgressBar mLoadingProgress;
    private RecyclerView rvBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        mLoadingProgress = findViewById(R.id.pb_loading);
        rvBooks = findViewById(R.id.rv_books);

        URL queryURL = (URL) getIntent().getSerializableExtra("Query");

        LinearLayoutManager booksLayoutManager = new LinearLayoutManager(this,
            RecyclerView.VERTICAL, false);
        rvBooks.setLayoutManager(booksLayoutManager);
        URL bookUrl;
        try {
            if (queryURL == null) {
                bookUrl = ApiUtil.buildUrl("android programming");
            } else {
                bookUrl = queryURL;
            }
            new BooksQueryTask().execute(bookUrl);
        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        ArrayList<String> recentList = SpUtil.getQueryList(getApplicationContext());
        int itemNum = recentList.size();
        for (int i = 0; i < itemNum; i++) {
             menu.add(Menu.NONE, i, Menu.NONE, recentList.get(i));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_advanced_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                int position = item.getItemId() + 1;
                String preferenceName = SpUtil.QUERY + position;
                String query = SpUtil.getPreferenceString(getApplicationContext(), preferenceName);
                String[] prefParams = query.split(",");
                String[] queryParams = new String[4];
                System.arraycopy(prefParams, 0, queryParams, 0, prefParams.length);
                URL bookUrl = ApiUtil.buildUrl(
                    (queryParams[0] == null) ? "" : queryParams[0],
                    (queryParams[1] == null) ? "" : queryParams[1],
                    (queryParams[2] == null) ? "" : queryParams[2],
                    (queryParams[3] == null) ? "" : queryParams[3]
                );
                new BooksQueryTask().execute(bookUrl);

                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            URL bookUrl = ApiUtil.buildUrl(query);
            new BooksQueryTask().execute(bookUrl);
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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
            TextView tvError = findViewById(R.id.tvError);
            if (result == null) {
                rvBooks.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.VISIBLE);
            } else {
                rvBooks.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.INVISIBLE);

                ArrayList<Book> books = ApiUtil.getBookFromJson(result);
                BookAdapter adapter = new BookAdapter(books);
                rvBooks.setAdapter(adapter);
            }
            mLoadingProgress.setVisibility(View.INVISIBLE);
        }

    }
}
