package com.nerone.instagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nerone.instagram.R;
import com.nerone.instagram.activities.HomeActivity;
import com.nerone.instagram.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PostFragment extends Fragment {
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;

    public final String TAG = "PostFragment";
    public String photoFileName = "photo.jpg";

    private ImageView ivPhotoTaken;
    private EditText etDescription;
    private Button btnSubmitPost;

    private File photoFile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivPhotoTaken = view.findViewById(R.id.ivPhotoTaken);
        etDescription = view.findViewById(R.id.etDescription);
        btnSubmitPost = view.findViewById(R.id.btnSubmitPost);

        setOnClickBtnSubmitPost();
        launchCamera();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_CANCELED) {
                returnToHome();
            }

            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivPhotoTaken.setImageBitmap(takenImage);
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setOnClickBtnSubmitPost() {
        btnSubmitPost.setOnClickListener((view) -> {
            ParseUser currentUser = ParseUser.getCurrentUser();
            final String description = etDescription.getText().toString();
            addPost(currentUser, description);
        });
    }

    private void addPost(ParseUser user, String description) {
        Post post = new Post();

        post.setUser(user);
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));

        post.saveInBackground((error) -> {
           if (error == null) {
                returnToHome();
           } else {
               error.printStackTrace();
           }
        });
    }

    private void returnToHome() {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
        //finish();
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.nerone.instagram.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "Failed to create directory");
        }

        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }
}
