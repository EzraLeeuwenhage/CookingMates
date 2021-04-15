package com.util;

import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.LoginActivity;
import com.ServerCallsApi;
import com.User;
import com.example.cookingmatesapp.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteAccountHelper {

    private AppCompatActivity activity;

    public DeleteAccountHelper(AppCompatActivity activity){
        this.activity = activity;
    }

    //Makes a call to the server to delete the user from the database
    public void deleteUser(ServerCallsApi api, User user){
        Call<Void> call = api.deleteUser(user.getUserId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //When user is successfully deleted from database,
                //show popup and go to LoginActivity
                if(response.isSuccessful()) {
                    Toast.makeText(activity.getApplicationContext(),
                            "Successfully deleted user", Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(activity.getApplicationContext(),
                            LoginActivity.class);
                    activity.startActivity(myIntent);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t)  {
                t.printStackTrace();
            }
        });
    }
}
