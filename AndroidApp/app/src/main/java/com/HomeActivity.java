package com;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.cookingmatesapp.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ImageButton recipe1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* Code for retrofit object creation
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://134.209.92.24/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServerCallsApi api = retrofit.create(ServerCallsApi.class);
         */

        recipe1 = (ImageButton)findViewById(R.id.img_btn);
        recipe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(HomeActivity.this, RecipeActivity.class);
                startActivity(myIntent);
            }
        });

    }
    
    /* Example of a request
    private void getUsers(){
        Call<List<User>> call = api.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(!response.isSuccessful()){
                    Log.e("ErrorTag","Code: " + response.code());
                    return;
                }

                //Result of the getUsers request
                List<User> users = response.body();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                //Something went wrong
            }
        });
    }*/
}