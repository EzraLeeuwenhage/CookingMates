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

    //Activity used to get context and start other activities
    private AppCompatActivity activity;

    //Constructor with activity, as it is needed for other methods
    public DeleteAccountHelper(AppCompatActivity activity){
        this.activity = activity;
    }

    //Makes a call to the server to delete the user from the database
    public void deleteUser(ServerCallsApi api, User user){
        Call<Void> call = api.deleteUser(user.getUserId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //Check if user is successfully deleted from database
                if(response.isSuccessful()) {
                    //Show popup
                    Toast.makeText(activity.getApplicationContext(),
                            "Successfully deleted user", Toast.LENGTH_SHORT).show();

                    //Start LoginActivity
                    Intent myIntent = new Intent(activity.getApplicationContext(),
                            LoginActivity.class);
                    activity.startActivity(myIntent);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t)  {
                //If call failed, print stack trace
                t.printStackTrace();
            }
        });
    }
}
