package com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.cookingmatesapp.R;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class RecipeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        Recipe recipe = (Recipe)intent.getParcelableExtra("recipe");
        if(recipe != null) {
            TextView text = findViewById(R.id.recipeTitle);
            text.setText(recipe.getName());
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