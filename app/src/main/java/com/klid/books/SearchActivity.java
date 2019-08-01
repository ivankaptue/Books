package com.klid.books;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final EditText etTitle = findViewById(R.id.etTitle);
        final EditText etAuthor = findViewById(R.id.etAuthors);
        final EditText etPublisher = findViewById(R.id.etPublisher);
        final EditText etISBN= findViewById(R.id.etIsbn);
        final Button searchButton = findViewById(R.id.btnSearh);
        searchButton.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String author = etAuthor.getText().toString().trim();
            String publisher = etPublisher.getText().toString().trim();
            String isbn = etISBN.getText().toString().trim();
            if (title.isEmpty() && author.isEmpty() && publisher.isEmpty() && isbn.isEmpty()) {
                String message = getString(R.string.no_search_data);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            } else {
                URL queryUrl = ApiUtil.buildUrl(title, author, publisher, isbn);
                Intent intent = new Intent(this, BookListActivity.class);
                intent.putExtra("Query", queryUrl);
                startActivity(intent);
            }
        });

    }
}
