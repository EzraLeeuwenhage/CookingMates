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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.example.cookingmatesapp.R;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.util.SearchHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchRecipe
        extends ActivityWithNavigation
        implements NavigationView.OnNavigationItemSelectedListener {

    private ServerCallsApi api;
    private SearchHelper searchHelper;
    private EditText editSearchBar;
    private List<Recipe> foundRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);

        //Define and set up navigation bar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarSearch);
        navigationView.setCheckedItem(R.id.nav_search_recipe);
        initNavigationBar();

        //Create ServerCallsApi object
        api = createApi();

        //Define search bar
        editSearchBar = findViewById(R.id.editSearchBar);

        //Define search helper
        searchHelper = new SearchHelper(this);
    }//end of OnCreate

    //Starts activity based on button clicked in navigation bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //Check if menu button clicked links to this activity
        switch(menuItem.getItemId()) {
            case R.id.nav_search_recipe:
                // Just break - same screen
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            default:
                //Use default implementation of method in ActivityWithNavigation
                super.onNavigationItemSelected(menuItem);
        }
        return true;
    }

    //Gets all recipes from database where the string in the search bar is a substring of
    // the recipe's name and puts the result in list foundRecipes
    public void getRecipeByTitle(View view){
        String title = editSearchBar.getText().toString();
        LinearLayout layout = findViewById(R.id.resultLayout);
        Call<List<Recipe>> call = api.getRecipeByTitle(title);
        searchHelper.makeCall(call, layout);
    }

    //Gets all recipes from database where the string in the search bar is a substring of
    // at least one of the recipe's ingredients and puts the result in list foundRecipes
    public void getRecipeByIngredient(View view){
        String ingredient = editSearchBar.getText().toString();
        LinearLayout layout = findViewById(R.id.resultLayout);
        Call<List<Recipe>> call = api.getRecipeByIngredient(ingredient);
        searchHelper.makeCall(call, layout);
    }

    //Gets all recipes from database where the string in the search bar is a substring of
    // at least one of the recipe's tags and puts the result in list foundRecipes
    public void getRecipeByTag(View view){
        String tag = editSearchBar.getText().toString();
        LinearLayout layout = findViewById(R.id.resultLayout);
        Call<List<Recipe>> call = api.getRecipeByTag(tag);
        searchHelper.makeCall(call, layout);
    }
}