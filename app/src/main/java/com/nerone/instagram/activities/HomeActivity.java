package com.nerone.instagram.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nerone.instagram.R;
import com.nerone.instagram.adapters.PostsAdapter;
import com.nerone.instagram.models.Post;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView rvPosts;
    private SwipeRefreshLayout swipeContainer;

    private List<Post> posts;
    private PostsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rvPosts = findViewById(R.id.rvPosts);
        swipeContainer = findViewById(R.id.swipeContainer);

        posts = new ArrayList<>();

        setSwipeContainer();
        setRecyclerView();
        getPosts();
    }

    private void setSwipeContainer() {
        swipeContainer.setOnRefreshListener(() -> {
            getPosts();
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void setRecyclerView() {
        adapter = new PostsAdapter(posts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getPosts() {
        ParseQuery<Post> postQuery = new ParseQuery<>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.orderByDescending("createdAt");

        postQuery.findInBackground((postsRes, error) -> {
            if (error == null) {
                adapter.clear();
                adapter.addAll(postsRes);
            }

            swipeContainer.setRefreshing(false);
        });
    }
}
