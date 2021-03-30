package com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.cookingmatesapp.R;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ToggleButton;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ServerCallsApi api;
    private List<Recipe> recommendedRecipes;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://134.209.92.24:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ServerCallsApi.class);


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

        getRecipes();
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
                // Just break - same screen
                break;
            case R.id.nav_profile:
                Intent profile_intent = new Intent(HomeActivity.this, ProfileActivity.class);
                profile_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(profile_intent);
                break;
            case R.id.nav_settings:
                Intent settings_intent = new Intent(HomeActivity.this, SettingsActivity.class);
                settings_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(settings_intent);
                break;
            case R.id.nav_about:
                Intent about_intent = new Intent(HomeActivity.this, AboutActivity.class);
                about_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(about_intent);
                break;
            case R.id.nav_help:
                Intent help_intent = new Intent(HomeActivity.this, HelpActivity.class);
                help_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(help_intent);
                break;
            case R.id.nav_logout:
                Intent logout_intent = new Intent(HomeActivity.this, LoginActivity.class);
                // this removes the User object attached to the context upon logging in
                logout_intent.getExtras().clear();
                logout_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout_intent);
                break;
            case R.id.nav_upload_recipe:
                Intent create_recipe_intent = new Intent(HomeActivity.this, CreateRecipeActivity.class);
                create_recipe_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(create_recipe_intent);
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

    public void openSearchRecipeActivity(View view){
        Intent search_recipe_intent = new Intent(HomeActivity.this, SearchRecipe.class);
        search_recipe_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(search_recipe_intent);
    }
    
    // Example of a request
    private void getRecipes(){
        Log.i("tag", "Recipes are being retrieved");
        Call<List<Recipe>> call = api.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(!response.isSuccessful()){
                    Log.e("ErrorTag","Code: " + response.code());
                    return;
                }else{
                    Log.i("SuccessTag", "Recipes retrieved");
                }

                //Result of the getRecipes request
                recommendedRecipes = response.body();
                createButtons();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getRecipesByName(View view){
        EditText name = findViewById(R.id.textInputEditText);
        Call<List<Recipe>> call = api.getRecipeByTitle(name.getText().toString());
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(!response.isSuccessful()){
                    Log.e("ErrorTag","Code: " + response.code());
                    return;
                }else{
                    Log.i("SuccessTag", "Recipes retrieved");
                }

                //Result of the getRecipes request
                recommendedRecipes = response.body();
                createButtons();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getImageInButton(ImageButton btn, Recipe recipe){
        String path = "http://134.209.92.24:3000/uploads/" + recipe.getFilename();
        Picasso.get().load(path).into(new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) { }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                Bitmap bitmapImage = bitmap;
                btn.setImageBitmap(bitmapImage);
                btn.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable arg0) { }
        });
    }

    private void createButtons(){
        if(recommendedRecipes != null) {
            LinearLayout ll = findViewById(R.id.recipeLayout);

            for (int i = 0; i < recommendedRecipes.size() && i < 10; i++) {
                ImageButton btn = new ImageButton(this);
                btn.setId(i);
                if(recommendedRecipes.get(i).getFilename() == null) {
                    btn.setImageResource(R.drawable.logo);
                }else{
                    getImageInButton(btn, recommendedRecipes.get(i));
                }
                btn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 400));
                btn.setPadding(0, 32, 0,0);
                ll.addView(btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, RecipeActivity.class);
                        intent.putExtra("recipe", recommendedRecipes.get(v.getId()));
                        startActivity(intent);
                    }
                });
            }
        }
    }
}