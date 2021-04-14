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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsActivity
        extends AppCompatActivity
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private ServerCallsApi api;
    private User user;
    private Button delete;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Create api object to make calls to server
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://134.209.92.24:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ServerCallsApi.class);

        //Define navigation bar
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

        //Define delete button
        delete = (Button) findViewById(R.id.delete_acc_btn);
        delete.setOnClickListener(this);

        setProfileInfo();
    };

    //Makes a deleteUser call when the delete account button is pressed
    @Override
    public void onClick (View v){
        switch (v.getId()) {
            case R.id.delete_acc_btn:
                // retrieve the user currently logged into
                user = getIntent().getParcelableExtra("user");

                // make a call to the server to delete the user from the database
                Call<Void> call = api.deleteUser(user.getUserId());

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Successfully deleted user", Toast.LENGTH_SHORT).show();

                            Intent myIntent = new Intent(SettingsActivity.this,
                                    LoginActivity.class);
                            startActivity(myIntent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t)  {
                        Toast.makeText(getApplicationContext(), t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
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
            case R.id.nav_home:
                startIntent(HomeActivity.class);
                break;
            case R.id.nav_profile:
                startIntent(ProfileActivity.class);
                break;
            case R.id.nav_settings:
                // Just break - same screen
                break;
            case R.id.nav_about:
                startIntent(AboutActivity.class);
                break;
            case R.id.nav_help:
                startIntent(HelpActivity.class);
                break;
            case R.id.nav_logout:
                startIntent(LoginActivity.class);
                break;
            case R.id.nav_upload_recipe:
                startIntent(CreateRecipeActivity.class);
                break;
            case R.id.nav_search_recipe:
                startIntent(SearchRecipe.class);
                break;
            case R.id.nav_findcookingmates:
                startIntent(FindCookingMatesActivity.class);
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

    //Retrieves user data from current intent and add the data to specified intent
    public void passUserObject(Intent myIntent) {
        Intent currentIntent = getIntent();
        User user = (User) currentIntent.getParcelableExtra("user");
        String cook = currentIntent.getStringExtra("cook");
        myIntent.putExtra("user", user);
        myIntent.putExtra("cook", cook);
    }

    //Displays user info
    public void setProfileInfo() {
        User user = getIntent().getParcelableExtra("user");

        //Username
        TextView view = findViewById(R.id.textView7);
        view.setText(user.getName());
        //Email
        TextView email = findViewById(R.id.textView8);
        email.setText(user.getEmail());
        //Password
        TextView password = findViewById(R.id.textView9);
        password.setText(user.getPassword());
        //Date of birth
        TextView date = findViewById(R.id.textView11);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String dateString = formatter.format(user.getDateOfBirth());
        date.setText(dateString);
    }

    //Starts a new intent to specified activity
    public <T extends AppCompatActivity> void startIntent(Class<T> className){
        Intent intent = new Intent(SettingsActivity.this, className);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        passUserObject(intent);
        startActivity(intent);
    }
}
