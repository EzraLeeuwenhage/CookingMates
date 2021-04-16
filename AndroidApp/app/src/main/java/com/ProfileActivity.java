package com;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.cookingmatesapp.R;
import com.google.android.material.navigation.NavigationView;
import com.util.DeleteAccountHelper;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProfileActivity
        extends ActivityWithNavigation
        implements NavigationView.OnNavigationItemSelectedListener {

    private ServerCallsApi api;
    private DeleteAccountHelper deleteAccountHelper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Define and set up navigation bar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarProfile);
        navigationView.setCheckedItem(R.id.nav_profile);
        initNavigationBar();

        //Create ServerCallsApi object
        api = createApi();

        //Define profileInfo helper
        deleteAccountHelper = new DeleteAccountHelper(this);

        setProfileInfo();
    }

    //Makes a deleteUser call when the delete account button is pressed
    public void deleteUser(View view){
        // retrieve the user currently logged into
        user = getIntent().getParcelableExtra("user");
        deleteAccountHelper.deleteUser(api, user);
    }

    //Starts activity based on button clicked in navigation bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //Check if menu button clicked links to this activity
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                // Just break - same screen
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            default:
                //Use default implementation of method in ActivityWithNavigation
                super.onNavigationItemSelected(menuItem);
        }
        return true;
    }

    //Display user info
    public void setProfileInfo() {
        User user = getIntent().getParcelableExtra("user");

        //Username
        TextView username = findViewById(R.id.textView);
        username.setText(user.getName());
        TextView view = findViewById(R.id.textView7);
        view.setText(user.getName());
        //Email
        TextView email = findViewById(R.id.textView8);
        email.setText(user.getEmail());
        //Date of birth
        TextView date = findViewById(R.id.textView9);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String dateString = formatter.format(user.getDateOfBirth());
        date.setText(dateString);
        //Account type
        TextView cook = findViewById(R.id.textView16);
        String cookString = getIntent().getStringExtra("cook");
        cook.setText(cookString);
        //Adult or child
        TextView account = findViewById(R.id.textView19);
        if (user.isAdult()) {
            account.setText("Adult");
        } else {
            account.setText("Child");
        }
    }
}