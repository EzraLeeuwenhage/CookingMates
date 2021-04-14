package com;

import androidx.annotation.NonNull;
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

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookingmatesapp.R;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProfileActivity
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
        setContentView(R.layout.activity_profile);

        //Create api object to make calls to server
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://134.209.92.24:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ServerCallsApi.class);

        //Define navigation bar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarProfile);

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
        navigationView.setCheckedItem(R.id.nav_profile);

        //Define delete button
        delete = (Button) findViewById(R.id.delete_acc_btn);
        delete.setOnClickListener(this);

        setProfileInfo();
    }

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

                            Intent myIntent = new Intent(ProfileActivity.this,
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
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent home_intent = new Intent(ProfileActivity.this, HomeActivity.class);
                home_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(home_intent);
                startActivity(home_intent);
                break;
            case R.id.nav_profile:
                // Just break - same screen
                break;
            case R.id.nav_settings:
                Intent settings_intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                settings_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(settings_intent);
                startActivity(settings_intent);
                break;
            case R.id.nav_about:
                Intent about_intent = new Intent(ProfileActivity.this, AboutActivity.class);
                about_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(about_intent);
                startActivity(about_intent);
                break;
            case R.id.nav_help:
                Intent help_intent = new Intent(ProfileActivity.this, HelpActivity.class);
                help_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(help_intent);
                startActivity(help_intent);
                break;
            case R.id.nav_logout:
                Intent logout_intent = new Intent(ProfileActivity.this, LoginActivity.class);
                logout_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout_intent);
                break;
            case R.id.nav_upload_recipe:
                Intent upload_recipe_intent = new Intent(ProfileActivity.this, CreateRecipeActivity.class);
                upload_recipe_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(upload_recipe_intent);
                startActivity(upload_recipe_intent);
                break;
            case R.id.nav_search_recipe:
                Intent search_recipe_intent = new Intent(ProfileActivity.this, SearchRecipe.class);
                search_recipe_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(search_recipe_intent);
                startActivity(search_recipe_intent);
                break;
            case R.id.nav_findcookingmates:
                Intent findcookingmates_intent = new Intent(ProfileActivity.this, FindCookingMatesActivity.class);
                findcookingmates_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(findcookingmates_intent);
                startActivity(findcookingmates_intent);
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

    //Display user info
    public void setProfileInfo() {
        User user = getIntent().getParcelableExtra("user");

        //Username
        TextView username = findViewById(R.id.textView);
        username.setText(user.getName());
        TextView view = findViewById(R.id.textView7);
        view.setText(user.getName());
        //Email
        TextView email = findViewById(R.id.textView8);
        email.setText(user.getEmail());
        //Date of birth
        TextView date = findViewById(R.id.textView9);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String dateString = formatter.format(user.getDateOfBirth());
        date.setText(dateString);
        //Account type
        TextView cook = findViewById(R.id.textView16);
        String cookString = getIntent().getStringExtra("cook");
        cook.setText(cookString);
        //Adult or child
        TextView account = findViewById(R.id.textView19);
        if (user.isAdult()) {
            account.setText("Adult");
        } else {
            account.setText("Child");
        }
    }

}