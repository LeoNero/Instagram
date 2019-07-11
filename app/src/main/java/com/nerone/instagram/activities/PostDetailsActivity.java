package com.nerone.instagram.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nerone.instagram.R;
import com.nerone.instagram.models.Post;
import com.nerone.instagram.utils.Time;
import com.parse.ParseQuery;

public class PostDetailsActivity extends AppCompatActivity {
    private ImageView ivPhoto;
    private TextView tvDescription;
    private TextView tvTimestamp;

    private String postId;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        ivPhoto = findViewById(R.id.ivPhoto);
        tvDescription = findViewById(R.id.tvDescription);
        tvTimestamp = findViewById(R.id.tvTimestamp);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        postId = getIntent().getStringExtra("post_id");
        fetchPost();
    }

    private void fetchPost() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.getInBackground(postId, (postRes, error) -> {
            if (error == null) {
                post = postRes;
                populateInformation();
            }
        });
    }

    private void populateInformation() {
        Glide.with(this)
                .load(post.getImage().getUrl())
                .into(ivPhoto);

        tvDescription.setText(post.getDescription());

        String createdAt = post.getCreatedAt().toString();
        tvTimestamp.setText(Time.getRelativeTimeAgo(createdAt));
    }
}
