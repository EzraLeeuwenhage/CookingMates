package com;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.cookingmatesapp.R;

import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.util.DeleteAccountHelper;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SettingsActivity
        extends ActivityWithNavigation
        implements NavigationView.OnNavigationItemSelectedListener {

    private User user;
    private ServerCallsApi api;
    private DeleteAccountHelper deleteAccountHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Define and set up navigation bar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarSettings);
        navigationView.setCheckedItem(R.id.nav_settings);
        initNavigationBar();

        //Create ServerCallsApi object
        api = createApi();

        //Define deleteAccount helper
        deleteAccountHelper = new DeleteAccountHelper(this);

        setSettingsInfo();
    };

    //Makes a deleteUser call when the delete account button is pressed
    public void deleteAccount(View view){
        // retrieve the user currently logged into
        user = getIntent().getParcelableExtra("user");
        deleteAccountHelper.deleteUser(api, user);
    }

    //Starts activity based on button clicked in navigation bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.nav_settings:
                // Just break - same screen
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            default:
                super.onNavigationItemSelected(menuItem);
        }
        return true;
    }

    //Displays user info
    public void setSettingsInfo() {
        User user = getIntent().getParcelableExtra("user");

        //Username
        TextView view = findViewById(R.id.textView7);
        view.setText(user.getName());
        //Email
        TextView email = findViewById(R.id.textView8);
        email.setText(user.getEmail());
        //Password
        TextView password = findViewById(R.id.textView9);
        password.setText(user.getPassword());
        //Date of birth
        TextView date = findViewById(R.id.textView11);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String dateString = formatter.format(user.getDateOfBirth());
        date.setText(dateString);
    }
}
