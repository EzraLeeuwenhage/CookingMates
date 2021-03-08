package com.example.nodejs_postgresql_android_test;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.nodejs_postgresql_android_test.retrofit.INodeJS;

public class CreateRecipeActivity extends AppCompatActivity {

    private INodeJS api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateRecipeActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.units));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner1 = (Spinner) findViewById(R.id.unit1);
        spinner1.setAdapter(adapter);
        Spinner spinner2 = (Spinner) findViewById(R.id.unit2);
        spinner2.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(INodeJS.class);
    }

    public void createRecipe(View view){
        /*
        //Title
        EditText editTitle = (EditText) findViewById(R.id.editTitle);
        String title = editTitle.getText().toString();
        //Description
        EditText editDescription = (EditText) findViewById(R.id.editDescription);
        String description = editDescription.getText().toString();

        //Ingredients
        EditText editIngredient1 = (EditText) findViewById(R.id.editTextIngredient1);
        String ingredient1 = editIngredient1.getText().toString();
        EditText editIngredient2 = (EditText) findViewById(R.id.editTextIngredient2);
        String ingredient2 = editIngredient2.getText().toString();

        //Steps
        EditText editStep1 = (EditText) findViewById(R.id.editTextStep1);
        String step1 = editStep1.getText().toString();
        EditText editStep2 = (EditText) findViewById(R.id.editTextStep2);
        String step2 = editStep2.getText().toString();

        Recipe recipe = new Recipe(title, description);
        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addStep(step1);
        recipe.addStep(step2);

        if(recipe.isValid()) {
            Intent intent = new Intent(this, ViewRecipeActivity.class);
            intent.putExtra(RECIPE, recipe);
            startActivity(intent);
        }*/

        //Title
        EditText editTitle = (EditText) findViewById(R.id.editTitle);
        String title = editTitle.getText().toString();
        //Description
        EditText editDescription = (EditText) findViewById(R.id.editDescription);
        String description = editDescription.getText().toString();

        TextView textViewCreated = (TextView) findViewById(R.id.textViewResult);

        RecipePost recipe = new RecipePost(title, description);
        Call<RecipePost> call = api.createRecipe(recipe);
        call.enqueue(new Callback<RecipePost>() {
            @Override
            public void onResponse(Call<RecipePost> call, Response<RecipePost> response) {
                if(!response.isSuccessful()){
                    textViewCreated.setText("Code: " + response.code());
                    return;
                }
                RecipePost recipe = response.body();
                textViewCreated.setText(recipe.getTitle() + " created!");
            }

            @Override
            public void onFailure(Call<RecipePost> call, Throwable t) {
                textViewCreated.setText(t.getMessage());
            }
        });
    }
}