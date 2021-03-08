package com.example.nodejs_postgresql_android_test;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.nodejs_postgresql_android_test.retrofit.INodeJS;

import java.util.List;

public class ViewRecipeActivity extends AppCompatActivity {
    private TextView textViewTitle;
    private TextView textViewDescription;

    private INodeJS api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getExtras().getParcelable(MenuActivity.RECIPE);

        //Title
        textViewTitle = findViewById(R.id.textViewResult);
        textViewTitle.setText(recipe.getTitle());

        //Description
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewDescription.setText(recipe.getDescription());

        //Ingredients
        TextView textViewIngredient1 = findViewById(R.id.textViewIngredient1);
        textViewIngredient1.setText(recipe.getIngredient(0));
        TextView textViewIngredient2 = findViewById(R.id.textViewIngredient2);
        textViewIngredient2.setText(recipe.getIngredient(1));

        //Steps
        TextView textViewStep1 = findViewById(R.id.textViewStep1);
        textViewStep1.setText(recipe.getStep(0));
        TextView textViewStep2 = findViewById(R.id.textViewStep2);
        textViewStep2.setText(recipe.getStep(1));


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(INodeJS.class);

        getRecipes();
        //getRecipeById(1);
        //getRecipeByTitle("Spaghetti");
    }

    private void getRecipes(){
        Call<List<RecipePost>> call = api.getPosts();
        call.enqueue(new Callback<List<RecipePost>>() {
            @Override
            public void onResponse(Call<List<RecipePost>> call, Response<List<RecipePost>> response) {
                if(!response.isSuccessful()){
                    textViewTitle.setText("Code: " + response.code());
                    return;
                }
                List<RecipePost> posts = response.body();
                RecipePost first = posts.get(1);
                textViewTitle.setText(first.getTitle());
                textViewDescription.setText(first.getDescription());
            }

            @Override
            public void onFailure(Call<List<RecipePost>> call, Throwable t) {
                textViewTitle.setText(t.getMessage());
            }
        });
    }

    private void getRecipeById(int id){
        Call<List<RecipePost>> call = api.getRecipe(id);
        call.enqueue(new Callback<List<RecipePost>>() {
            @Override
            public void onResponse(Call<List<RecipePost>> call, Response<List<RecipePost>> response) {
                if(!response.isSuccessful()){
                    textViewTitle.setText("Code: " + response.code());
                    return;
                }
                List<RecipePost> posts = response.body();
                RecipePost first = posts.get(0);
                textViewTitle.setText(first.getTitle());
                textViewDescription.setText(first.getDescription());
            }

            @Override
            public void onFailure(Call<List<RecipePost>> call, Throwable t) {
                textViewTitle.setText(t.getMessage());
            }
        });
    }

    private void getRecipeByTitle(String title) {
        Call<List<RecipePost>> call = api.getRecipeByTitle(title);
        call.enqueue(new Callback<List<RecipePost>>() {
            @Override
            public void onResponse(Call<List<RecipePost>> call, Response<List<RecipePost>> response) {
                if (!response.isSuccessful()) {
                    textViewTitle.setText("Code: " + response.code());
                    return;
                }
                List<RecipePost> posts = response.body();
                if (posts.size() > 0) {
                    RecipePost first = posts.get(0);
                    textViewTitle.setText(first.getTitle());
                    textViewDescription.setText(first.getDescription());
                }
            }

            @Override
            public void onFailure(Call<List<RecipePost>> call, Throwable t) {
                textViewTitle.setText(t.getMessage());
            }
        });
    }
}