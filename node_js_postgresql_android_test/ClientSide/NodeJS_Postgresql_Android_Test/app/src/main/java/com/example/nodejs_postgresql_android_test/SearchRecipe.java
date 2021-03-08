package com.example.nodejs_postgresql_android_test;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nodejs_postgresql_android_test.retrofit.INodeJS;

import java.util.List;

public class SearchRecipe extends AppCompatActivity {

    private INodeJS api;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private EditText editTextId;
    private EditText editTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(INodeJS.class);

        textViewTitle = (TextView) findViewById(R.id.textViewResult);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        editTextId = (EditText) findViewById(R.id.editTextNumber);
        editTextTitle = (EditText) findViewById(R.id.editTitle);
    }

    public void getRecipes(View view){
        Call<List<RecipePost>> call = api.getPosts();
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

    public void getRecipeById(View view){
        int id = Integer.parseInt(editTextId.getText().toString());
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

    public void getRecipeByTitle(View view){
        String title = editTextTitle.getText().toString();
        Call<List<RecipePost>> call = api.getRecipeByTitle(title);
        call.enqueue(new Callback<List<RecipePost>>() {
            @Override
            public void onResponse(Call<List<RecipePost>> call, Response<List<RecipePost>> response) {
                if(!response.isSuccessful()){
                    textViewTitle.setText("Code: " + response.code());
                    return;
                }
                List<RecipePost> posts = response.body();
                if(posts.size() > 0) {
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