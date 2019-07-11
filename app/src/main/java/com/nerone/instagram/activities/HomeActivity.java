package com.nerone.instagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nerone.instagram.R;
import com.nerone.instagram.adapters.PostsAdapter;
import com.nerone.instagram.models.Post;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

        posts = new ArrayList<>();

        setSwipeContainer();
        setRecyclerView();
        getPosts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.photo:
                goToAddPost();
                return true;
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void goToAddPost() {
        Intent intent = new Intent(HomeActivity.this, PostActivity.class);
        startActivity(intent);
    }

    private void logout() {
        ParseUser.logOut();

        goToLoginScreen();
    }

    private void goToLoginScreen() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);

        finish();
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
