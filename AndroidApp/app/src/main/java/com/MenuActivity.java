package com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cookingmatesapp.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    public void search(View view){
        Intent intent = new Intent(this, SearchRecipe.class);
        startActivity(intent);
    }

    public void updateOrDelete(View view){
        Intent intent = new Intent(this, UpdateDeleteRecipeActivity.class);
        startActivity(intent);
    }

    public void createRecipe(View view){
        Intent intent = new Intent(this, CreateRecipeActivity.class);
        startActivity(intent);
    }
}
