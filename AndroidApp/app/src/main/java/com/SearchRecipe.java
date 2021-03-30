package com;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cookingmatesapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class SearchRecipe extends AppCompatActivity {

    private ServerCallsApi api;
    private EditText editSearchBar;

    private Bitmap bitmapImage;
    private List<Recipe> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://134.209.92.24:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ServerCallsApi.class);

        editSearchBar = findViewById(R.id.editSearchBar);
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

    //Actually makes the call for the get methods
    public void makeCall(Call<List<Recipe>> call){
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_SHORT);
                    return;
                }
                recipes = response.body();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
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
}