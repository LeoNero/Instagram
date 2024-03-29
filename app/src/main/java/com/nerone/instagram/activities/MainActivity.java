package com.nerone.instagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.nerone.instagram.R;
import com.nerone.instagram.fragments.HomeFragment;
import com.nerone.instagram.fragments.PostFragment;
import com.nerone.instagram.fragments.ProfileFragment;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setInitialFragment();
        setBottomNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setInitialFragment() {
        goToHome();
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }

    private void setBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    goToHome();
                    break;
                case R.id.action_post:
                    goToAddPost();
                    break;
                case R.id.action_profile:
                    goToProfile();
                    break;
                default:
                    break;
            }

            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();

            return true;
        });
    }

    private void goToHome() {
        fragment = new HomeFragment();
    }

    private void goToAddPost() {
        fragment = new PostFragment();
    }

    private void goToProfile() {
        fragment = new ProfileFragment();
    }

    private void logout() {
        ParseUser.logOut();

        goToLoginScreen();
    }

    private void goToLoginScreen() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

        finish();
    }
}
