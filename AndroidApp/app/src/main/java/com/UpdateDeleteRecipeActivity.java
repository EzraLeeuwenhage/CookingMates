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

import java.util.ArrayList;
import java.util.List;

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
        //TODO update only required fields
        Recipe updatedRecipe = new Recipe(0, "name", "description",
                new ArrayList<>(),  new ArrayList<>(), 0, false, new ArrayList<>());
        updatedRecipe.setFilename("");
        Call<Recipe> call = api.putRecipe(id, updatedRecipe);
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                Recipe recipe = response.body();
                textViewResult.setText(recipe.getName() + " updated!");
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
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