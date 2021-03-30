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
        toolbar = findViewById(R.id.toolbarSettings);

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
        navigationView.setCheckedItem(R.id.nav_settings);

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
        switch(menuItem.getItemId()) {
            case R.id.nav_home:
                Intent home_intent = new Intent(SettingsActivity.this, HomeActivity.class);
                home_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(home_intent);
                startActivity(home_intent);
                break;
            case R.id.nav_profile:
                Intent profile_intent = new Intent(SettingsActivity.this, ProfileActivity.class);
                profile_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(profile_intent);
                startActivity(profile_intent);
                break;
            case R.id.nav_settings:
                // Just break - same screen
                break;
            case R.id.nav_about:
                Intent about_intent = new Intent(SettingsActivity.this, AboutActivity.class);
                about_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(about_intent);
                startActivity(about_intent);
                break;
            case R.id.nav_help:
                Intent help_intent = new Intent(SettingsActivity.this, HelpActivity.class);
                help_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(help_intent);
                startActivity(help_intent);
                break;
            case R.id.nav_logout:
                Intent logout_intent = new Intent(SettingsActivity.this, LoginActivity.class);
                logout_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout_intent);
                break;
            case R.id.nav_upload_recipe:
                Intent upload_recipe_intent = new Intent(SettingsActivity.this, CreateRecipeActivity.class);
                upload_recipe_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(upload_recipe_intent);
                startActivity(upload_recipe_intent);
                break;
            case R.id.nav_search_recipe:
                Intent search_recipe_intent = new Intent(SettingsActivity.this, SearchRecipe.class);
                search_recipe_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(search_recipe_intent);
                startActivity(search_recipe_intent);
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

    public void passUserObject(Intent myIntent) {
        Intent currentIntent = getIntent();
        User user = (User) currentIntent.getParcelableExtra("user");
        myIntent.putExtra("user", user);
    }
}
