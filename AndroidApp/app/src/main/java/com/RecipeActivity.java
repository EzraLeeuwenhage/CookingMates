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

import com.example.cookingmatesapp.R;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class RecipeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Recipe recipe;
    private ServerCallsApi api;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://134.209.92.24:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ServerCallsApi.class);

        Intent intent = getIntent();
        Recipe recipe = (Recipe)intent.getParcelableExtra("recipe");
        if(recipe != null) {
            TextView text = findViewById(R.id.recipeTitle);
            text.setText(recipe.getName());

            TextView rating = findViewById(R.id.recipeRating);
            if(recipe.hasRating()) {
                rating.setText("" + recipe.getRating());
            }else{
                rating.setText("-");
            }

            ImageView image = findViewById(R.id.recipeImage);
            getImage(image, recipe);

            TextView nrPeople = findViewById(R.id.editNrPeople);
            nrPeople.setText(" " + recipe.getNumberpeople());

            if(recipe.getIngredients() != null && recipe.getQuantities() != null) {
                LinearLayout ll = (LinearLayout) findViewById(R.id.ingredientsLayout);
                for (int i = 0; i < recipe.getIngredients().size(); i++) {
                    String ingredient = recipe.getQuantities().get(i) + " " + recipe.getIngredients().get(i);
                    TextView tv = new TextView(this);
                    tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    tv.setText(ingredient);
                    if(ll != null) {
                        ll.addView(tv);
                    }
                }
            }

            TextView description = findViewById(R.id.recipeDescription);
            description.setText(recipe.getDescription());

            if(recipe.getReviews() != null){
                LinearLayout ll = findViewById(R.id.reviewLayout);
                for (int i = 0; i < recipe.getIngredients().size(); i++) {
                    String review = recipe.getReviews().get(i);
                    TextView tv = new TextView(this);
                    tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    tv.setText(review);
                    if(ll != null) {
                        ll.addView(tv);
                    }
                }
            }
        }

        // Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarRecipe);

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

    public void sendRating(View view){
        int rating = 0;
        switch (view.getId()){
            case R.id.rating1:
                rating = 1;
                break;
            case R.id.rating2:
                rating = 2;
                break;
            case R.id.rating3:
                rating = 3;
                break;
            case R.id.rating4:
                rating = 4;
                break;
            case R.id.rating5:
                rating = 5;
                break;
        }

        if(rating != 0){
            recipe.addRating(rating);
        }

        Call<Void> call = api.addRatingToRecipe(this.recipe.getRecipeId(), this.recipe.getRatings());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(),"Rating added!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendReview(View view){
        EditText editReview = findViewById(R.id.editReview);
        String review = editReview.getText().toString();
        recipe.addReview(review);

        Call<Void> call = api.addReviewToRecipe(recipe.getRecipeId(), recipe.getReviews());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(),"Review added!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getImage(ImageView v, Recipe recipe){
        String path = "http://134.209.92.24:3000/uploads/" + recipe.getFilename();
        Picasso.get().load(path).into(new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) { }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                Bitmap bitmapImage = bitmap;
                v.setImageBitmap(bitmapImage);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable arg0) { }
        });
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
                Intent home_intent = new Intent(RecipeActivity.this, HomeActivity.class);
                home_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(home_intent);
                break;
            case R.id.nav_profile:
                Intent profile_intent = new Intent(RecipeActivity.this, ProfileActivity.class);
                profile_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(profile_intent);
                break;
            case R.id.nav_settings:
                Intent settings_intent = new Intent(RecipeActivity.this, SettingsActivity.class);
                settings_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(settings_intent);
                break;
            case R.id.nav_about:
                Intent about_intent = new Intent(RecipeActivity.this, AboutActivity.class);
                about_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(about_intent);
                break;
            case R.id.nav_help:
                Intent help_intent = new Intent(RecipeActivity.this, HelpActivity.class);
                help_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(help_intent);
                break;
            case R.id.nav_logout:
                Intent logout_intent = new Intent(RecipeActivity.this, LoginActivity.class);
                logout_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout_intent);
                break;
            case R.id.nav_upload_recipe:
                Intent create_recipe_intent = new Intent(RecipeActivity.this, CreateRecipeActivity.class);
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
}