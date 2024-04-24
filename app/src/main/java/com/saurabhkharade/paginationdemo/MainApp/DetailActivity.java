package com.saurabhkharade.paginationdemo.MainApp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.saurabhkharade.paginationdemo.Model.Post;
import com.saurabhkharade.paginationdemo.R;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POST = "com.saurabhkharade.paginationdemo.EXTRA_POST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Get the post from the intent
        Post post = getIntent().getParcelableExtra(EXTRA_POST);

        // Display the post details in your UI
        TextView titleTextView = findViewById(R.id.tv_title);
        TextView bodyTextView = findViewById(R.id.tv_body);
        TextView tvgetId = findViewById(R.id.tv_id);
        TextView tv_userid = findViewById(R.id.tv_userid);

        titleTextView.setText("Title : " +post.getTitle());
        bodyTextView.setText("Body : " +post.getBody());
        tv_userid.setText("User Id : " +String.valueOf(post.getUserId()));
        tvgetId.setText("Id : " +String.valueOf(post.getId()));

    }
}
