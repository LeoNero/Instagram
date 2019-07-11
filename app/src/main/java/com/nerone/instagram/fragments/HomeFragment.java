package com.nerone.instagram.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nerone.instagram.R;
import com.nerone.instagram.adapters.PostsAdapter;
import com.nerone.instagram.models.Post;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView rvPosts;
    protected SwipeRefreshLayout swipeContainer;

    private List<Post> posts;
    protected PostsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);
        swipeContainer = view.findViewById(R.id.swipeContainer);

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
        adapter = new PostsAdapter(getContext(), posts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    protected void getPosts() {
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
