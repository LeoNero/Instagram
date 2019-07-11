package com.nerone.instagram.fragments;

import com.nerone.instagram.models.Post;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ProfileFragment extends HomeFragment {
    @Override
    protected void getPosts() {
        ParseQuery<Post> postQuery = new ParseQuery<>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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
