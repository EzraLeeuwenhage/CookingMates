package com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.cookingmatesapp.R;
import com.google.android.material.navigation.NavigationView;

public class AboutActivity
        extends ActivityWithNavigation
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create activity and set content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Define and set up navigation bar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarAbout);
        navigationView.setCheckedItem(R.id.nav_about);
        initNavigationBar();
    }

    //Starts activity based on button clicked in navigation bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //Check if menu button clicked links to this activity
        switch(menuItem.getItemId()) {
            case R.id.nav_about:
                // Just break - same screen
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            default:
                //Use default implementation of method in ActivityWithNavigation
                super.onNavigationItemSelected(menuItem);
        }
        return true;
    }
}