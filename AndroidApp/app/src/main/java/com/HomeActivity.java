package com;

import androidx.appcompat.app.AppCompatActivity;
import com.example.cookingmatesapp.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {

    private ImageButton recipe1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recipe1 = (ImageButton)findViewById(R.id.img_btn);
        recipe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(HomeActivity.this, RecipeActivity.class);
                startActivity(myIntent);
            }
        });

    }
}