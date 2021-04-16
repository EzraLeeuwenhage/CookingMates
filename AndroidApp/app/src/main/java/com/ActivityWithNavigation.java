package com;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.cookingmatesapp.R;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityWithNavigation
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Toolbar toolbar;

    //Setup navigation bar
    public void initNavigationBar(){
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
    }

    //Creates api object to make calls to server
    public ServerCallsApi createApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://134.209.92.24:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ServerCallsApi.class);
    }

    //Toggles navigation bar
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Starts activity based on button clicked in navigation bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            //Home Activity
            case R.id.nav_home:
                startIntent(HomeActivity.class);
                break;
            //Profile Activity
            case R.id.nav_profile:
                startIntent(ProfileActivity.class);
                break;
            //Settings Activity
            case R.id.nav_settings:
                startIntent(SettingsActivity.class);
                break;
            //About Activity
            case R.id.nav_about:
                startIntent(AboutActivity.class);
                break;
            //Help Activity
            case R.id.nav_help:
                startIntent(HelpActivity.class);
                break;
            //Logout
            case R.id.nav_logout:
                startIntent(LoginActivity.class);
                break;
            //CreateRecipe Activity
            case R.id.nav_upload_recipe:
                startIntent(CreateRecipeActivity.class);
                break;
            //SearchRecipe Activity
            case R.id.nav_search_recipe:
                startIntent(SearchRecipe.class);
                break;
            //FindCookingMates Activity
            case R.id.nav_findcookingmates:
                startIntent(FindCookingMatesActivity.class);
                break;
            //Contact Activity
            case R.id.nav_contact:
                break;
            //Link to instagram page
            case R.id.nav_instagram:
                break;
            //Link to facebook page
            case R.id.nav_facebook:
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //Starts a new intent to specified activity
    public <T extends AppCompatActivity> void startIntent(Class<T> className){
        Intent intent = new Intent(getApplicationContext(), className);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        passUserObject(intent);
        startActivity(intent);
    }

    //Retrieves user data from current intent and add the data to specified intent
    public void passUserObject(Intent myIntent) {
        Intent currentIntent = getIntent();
        User user = (User) currentIntent.getParcelableExtra("user");
        String cook = currentIntent.getStringExtra("cook");
        myIntent.putExtra("user", user);
        myIntent.putExtra("cook", cook);
    }
}
