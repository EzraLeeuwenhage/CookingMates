package com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.example.cookingmatesapp.R;

import android.content.Intent;
import android.view.View;

import com.example.cookingmatesapp.MainActivity;
import com.google.android.material.navigation.NavigationView;

public class SettingsActivity
        extends AppCompatActivity
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private Button deleteAcc;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Navigation
        // Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        // define login button
        deleteAcc = (Button) findViewById(R.id.delete_acc_btn);
        deleteAcc.setOnClickListener(this);

    };

    @Override
    public void onClick (View v){
        Intent myIntent = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(myIntent);
        //optional
        finish();
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // TODO add navigation
        switch(menuItem.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_profile:
                break;
            case R.id.nav_settings:
                // Just break - same screen
                break;
            case R.id.nav_about:
                break;
            case R.id.nav_help:
                break;
            case R.id.nav_logout:
                break;
            case R.id.nav_upload_recipe:
                break;
            case R.id.nav_findcookingmates:
                break;
            case R.id.nav_contact:
                break;
            case R.id.nav_instagram:
                break;
            case R.id.nav_facebook:
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
