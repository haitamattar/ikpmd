package com.school.haitamelattar.freeob;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import static android.R.attr.name;

public class SettingActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    Button logoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //menu
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerSetting);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.navigationSetting);
        navigationView.setCheckedItem(R.id.navSettings);
        setupDrawerContent(navigationView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        logoutBtn = (Button) findViewById(R.id.buttonLogOut);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/AvenirNextLTPro-Regular.ttf");
        logoutBtn.setTypeface(typeface);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                settings.edit().remove("loginToken").commit();
                settings.edit().remove("email").commit();
                settings.edit().remove("id").commit();
                // Go to login page
                Intent home = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(home);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) return true;

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles the navigation menu Items on SettingActivity
     * @param navigationView The navigation menu view
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        Intent intent;

                        switch (menuItem.getItemId()){
                            case R.id.navAdverts:
                                intent = new Intent(SettingActivity.this, AdvertsActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.navAddAdvert:
                                intent = new Intent(SettingActivity.this, AddAdvertActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.navProfile:
                                intent = new Intent(SettingActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.navSettings:
                                return false;
                        }

                        return true;
                    }
                }
        );
    }
}
