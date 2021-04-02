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
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cookingmatesapp.R;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class SearchRecipe extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ServerCallsApi api;
    private EditText editSearchBar;

    private Bitmap bitmapImage;
    private List<Recipe> foundRecipes = new ArrayList<>();

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://134.209.92.24:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ServerCallsApi.class);

        // Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarSearch);

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

        editSearchBar = findViewById(R.id.editSearchBar);
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
                Intent home_intent = new Intent(SearchRecipe.this, HomeActivity.class);
                home_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(home_intent);
                startActivity(home_intent);
                break;
            case R.id.nav_profile:
                Intent profile_intent = new Intent(SearchRecipe.this, ProfileActivity.class);
                profile_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(profile_intent);
                startActivity(profile_intent);
                break;
            case R.id.nav_settings:
                Intent settings_intent = new Intent(SearchRecipe.this, SettingsActivity.class);
                settings_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(settings_intent);
                startActivity(settings_intent);
                break;
            case R.id.nav_about:
                Intent about_intent = new Intent(SearchRecipe.this, AboutActivity.class);
                about_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(about_intent);
                startActivity(about_intent);
                break;
            case R.id.nav_help:
                Intent help_intent = new Intent(SearchRecipe.this, HelpActivity.class);
                help_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(help_intent);
                startActivity(help_intent);
                break;
            case R.id.nav_logout:
                Intent logout_intent = new Intent(SearchRecipe.this, LoginActivity.class);
                logout_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout_intent);
                break;
            case R.id.nav_upload_recipe:
                Intent create_recipe_intent = new Intent(SearchRecipe.this, CreateRecipeActivity.class);
                create_recipe_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(create_recipe_intent);
                startActivity(create_recipe_intent);
                break;
            case R.id.nav_search_recipe:
                // Just break - same screen
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

    //Get methods get recipes from server database and put them in recipes list

    //Gets all recipes
    public void getRecipes(View view){
        Call<List<Recipe>> call = api.getRecipes();
        makeCall(call);
    }

    //Gets all recipes where the search string is in the name
    public void getRecipeByTitle(View view){
        String title = editSearchBar.getText().toString();
        Call<List<Recipe>> call = api.getRecipeByTitle(title);
        makeCall(call);
    }

    //Gets all recipes where the search string is in at least one ingredient
    public void getRecipeByIngredient(View view){
        String ingredient = editSearchBar.getText().toString();
        Call<List<Recipe>> call = api.getRecipeByIngredient(ingredient);
        makeCall(call);
    }

    //Gets all recipes where the search string is in at least one tag
    public void getRecipeByTag(View view){
        String tag = editSearchBar.getText().toString();
        Call<List<Recipe>> call = api.getRecipeByTag(tag);
        makeCall(call);
    }

    //Actually makes the call for the get methods
    public void makeCall(Call<List<Recipe>> call){
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                foundRecipes = response.body();
                List<Recipe> recipes = new ArrayList<>();
                User user = getIntent().getParcelableExtra("user");
                if (!user.isAdult()) {
                    for (Recipe recipe: foundRecipes) {
                        if (!recipe.isForAdult()) {
                            recipes.add(recipe);
                        }
                    }
                    foundRecipes = recipes;
                }
                createButtons(foundRecipes, findViewById(R.id.resultLayout));
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Retrieves the image of the input recipe and places it in the input view
    public void getImage(ImageView view, Recipe recipe){
        String path = "http://134.209.92.24:3000/uploads" + recipe.getFilename();
        Picasso.get().load(path).into(new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) { }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                bitmapImage = bitmap;
                view.setImageBitmap(bitmapImage);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable arg0) { }
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

    public void createButtons(List<Recipe> list, LinearLayout layout) {
            layout.removeAllViews();

        if(list != null) {
            for (int i = 0; i < list.size() && i < 10; i++) {
                ImageButton btn = new ImageButton(this);
                btn.setId(i);
                if (list.get(i).getFilename() == null) {
                    btn.setImageResource(R.drawable.logo);
                } else {
                    getImageInButton(btn, list.get(i));
                }
                btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400));
                btn.setPadding(0, 32, 0,0);
                layout.addView(btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchRecipe.this, RecipeActivity.class);
                        intent.putExtra("recipe", foundRecipes.get(v.getId()));
                        passUserObject(intent);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}