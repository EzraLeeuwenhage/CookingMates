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
import com.util.SearchHelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity
        extends ActivityWithNavigation
        implements NavigationView.OnNavigationItemSelectedListener {

    private ServerCallsApi api;
    private SearchHelper searchHelper;
    private List<Recipe> recommendedRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Define and set up navigation bar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarHome);
        navigationView.setCheckedItem(R.id.nav_home);
        initNavigationBar();

        //Create ServerCallsApi object
        api = createApi();

        //Define search helper
        searchHelper = new SearchHelper(getIntent(), this);

        //Show all recipes
        getRecipes();
    }

    //Starts activity based on button clicked in navigation bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.nav_home:
                // Just break - same screen
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            default:
                super.onNavigationItemSelected(menuItem);
        }
        return true;
    }

    //Starts search recipe activity when button is pressed
    public void openSearchRecipeActivity(View view){
        startIntent(SearchRecipe.class);
    }
    
    //Gets all recipes from database, then calls filterRecipes() and createButtons() on success
    private void getRecipes(){
        LinearLayout layout = findViewById(R.id.recipeLayout);
        Call<List<Recipe>> call = api.getRecipes();
        searchHelper.makeCall(call, layout, recommendedRecipes);
    }

    //Gets all recipes from database, where the string in the search bar editText is a substring
    // of the recipe's name, then calls filterRecipes() and createButtons() on success
    //Is called when the search button is pressed
    public void getRecipesByName(View view){
        EditText name = findViewById(R.id.textInputEditText);
        LinearLayout layout = findViewById(R.id.recipeLayout);
        Call<List<Recipe>> call = api.getRecipeByTitle(name.getText().toString());
        searchHelper.makeCall(call, layout, recommendedRecipes);
    }
}