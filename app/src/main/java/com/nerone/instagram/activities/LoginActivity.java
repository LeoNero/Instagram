package com.nerone.instagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.nerone.instagram.R;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        verifyIfUserIsLoggedAndRedirect();

        setBtnLoginClick();
        setBtnSignupClick();
    }

    private void verifyIfUserIsLoggedAndRedirect() {
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            goToHome();
        }
    }

    private void goToHome() {
        final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setBtnLoginClick() {
        btnLogin.setOnClickListener((view) -> {
            final String username = etUsername.getText().toString();
            final String password = etPassword.getText().toString();

            login(username, password);
        });
    }

    private void setBtnSignupClick() {
        btnSignup.setOnClickListener((view) -> {
            goToSignup();
        });
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, (user, error) -> {
            if (user != null) {
                goToHome();
            } else {
                error.printStackTrace();
            }
        });
    }

    private void goToSignup() {
        final Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}
