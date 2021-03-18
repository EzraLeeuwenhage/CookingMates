package com;

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

import com.example.cookingmatesapp.R;

public class UpdateDeleteRecipeActivity extends AppCompatActivity {

    private ServerCallsApi api;
    private TextView textViewResult;
    private EditText editTextId;
    private EditText editTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_recipe);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://134.209.92.24:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ServerCallsApi.class);

        textViewResult = (TextView) findViewById(R.id.textViewResult);
        editTextId = (EditText) findViewById(R.id.editTextNumber);
        editTextTitle = (EditText) findViewById(R.id.editTitle);
    }

    public void updateRecipe(View view){
        int id = Integer.parseInt(editTextId.getText().toString());
        String title = editTextTitle.getText().toString();
        Call<RecipePost> call = api.putRecipe(id, new RecipePost(title, "default description", "a9e982fd5dd113212f5b5792a90a6a92.png", "uploads/"));
        call.enqueue(new Callback<RecipePost>() {
            @Override
            public void onResponse(Call<RecipePost> call, Response<RecipePost> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                RecipePost recipe = response.body();
                textViewResult.setText(recipe.getTitle() + " updated!");
            }

            @Override
            public void onFailure(Call<RecipePost> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    public void deleteRecipe(View view) {
        int id = Integer.parseInt(editTextId.getText().toString());
        Call<Void> call = api.deleteRecipe(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                textViewResult.setText("Recipe deleted!");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}