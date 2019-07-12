package com.nerone.instagram.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nerone.instagram.R;
import com.nerone.instagram.models.Post;
import com.nerone.instagram.utils.Time;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {
    private ImageView ivPhoto;
    private TextView tvDescription;
    private TextView tvTimestamp;
    private TextView tvLikes;
    private TextView tvLikedOrNot;
    private FloatingActionButton fab;

    private String postId;
    private Post post;

    private boolean likedOrNot;
    private int likeCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        ivPhoto = findViewById(R.id.ivPhoto);
        tvDescription = findViewById(R.id.tvDescription);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        tvLikes = findViewById(R.id.tvLikes);
        tvLikedOrNot = findViewById(R.id.tvLikedOrNot);

        ivPhoto.setScaleType(ImageView.ScaleType.FIT_XY);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);

        likedOrNot = false;

        postId = getIntent().getStringExtra("post_id");
        fetchPost();
        setFabClick();
    }

    private void fetchPost() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.getInBackground(postId, (postRes, error) -> {
            if (error == null) {
                post = postRes;

                fetchLikes();
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

    private void fetchLikes() {
        post.getLikesRelation().getQuery().findInBackground((results, error) -> {
            if (error == null) {
                if (results == null || results.size() == 0) {
                    tvLikes.setText("No likes");
                    tvLikedOrNot.setText("Not liked!");
                } else if (results.size() == 1) {
                    likeCounter = 1;
                    updateLikeCounterText();
                    checkIfUserLiked(results);
                } else {
                    likeCounter = results.size();
                    updateLikeCounterText();
                    checkIfUserLiked(results);
                }

            } else {
                error.printStackTrace();
            }
        });
    }

    private void updateLikeCounterText() {
        if (likeCounter == 0) {
            tvLikes.setText("No likes");
        } else if (likeCounter == 1) {
            tvLikes.setText("One like");
        } else {
            tvLikes.setText(likeCounter + " likes");
        }
    }

    private void checkIfUserLiked(List<ParseUser> results) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        String currentUserId = currentUser.getObjectId();

        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getObjectId().equals(currentUserId)) {
                likedOrNot = true;
            }
        }

        changeLikedOrNotText();
    }

    private void changeLikedOrNotText() {
        if (likedOrNot) {
            tvLikedOrNot.setText("Liked!");
        } else {
            tvLikedOrNot.setText("Not liked!");
        }
    }

    private void setFabClick() {
        fab.setOnClickListener((view) -> {
            if (likedOrNot) {
                unlikePost();
            } else {
                likePost();
            }
        });
    }

    private void unlikePost() {
        post.removeLike(ParseUser.getCurrentUser());
        post.saveInBackground((error) -> {
            if (error == null) {
                likedOrNot = false;
                likeCounter = likeCounter - 1;
                updateLikeCounterText();
                changeLikedOrNotText();
            }
        });
    }

    private void likePost() {
        post.addLike(ParseUser.getCurrentUser());
        post.saveInBackground((error) -> {
            if (error == null) {
                likedOrNot = true;
                likeCounter = likeCounter + 1;
                updateLikeCounterText();
                changeLikedOrNotText();
            }
        });
    }
}
