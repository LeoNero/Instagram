package com.nerone.instagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nerone.instagram.R;
import com.parse.ParseUser;

public class SignupActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);

        setBtnSignupClick();
    }

    private void setBtnSignupClick() {
        btnSignup.setOnClickListener((view) -> {
            final String email = etEmail.getText().toString();
            final String username = etUsername.getText().toString();
            final String password = etPassword.getText().toString();

            signup(email, username, password);
        });
    }

    private void signup(String email, String username, String password) {
        ParseUser user = new ParseUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground((error) -> {
            if (error == null) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Error creating account, try again...", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }
}
