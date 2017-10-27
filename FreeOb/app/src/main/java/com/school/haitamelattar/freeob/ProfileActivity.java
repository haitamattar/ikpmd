package com.school.haitamelattar.freeob;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.school.haitamelattar.freeob.model.User;

import org.json.JSONObject;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    User userInfo;
    RequestQueue requestQueue;
    TextView name, bio;
    ImageView profilePicture;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.setTitle("Profile");

        // Get data
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        // Check if user has an account, if so go to all adverts activity
        SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String token = settings.getString("loginToken", "");
        String email = settings.getString("email", "");
        String userid = settings.getString("id", "");
        getUserData(token, email, userid, requestQueue);

        // Set data into page
        name = (TextView) findViewById(R.id.textViewName);
        bio = (TextView) findViewById(R.id.textViewBio);
        profilePicture = (ImageView) findViewById(R.id.profileImage);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/AvenirNextLTPro-Regular.ttf");
        name.setTypeface(typeface);
        bio.setTypeface(typeface);

        String urlProfileImage = "https://pbs.twimg.com/profile_images/378800000855396544/770e7d1bbd0138d276ad372d317145f8.jpeg";

        Glide.with(this).load(urlProfileImage).apply(RequestOptions.circleCropTransform()).into(profilePicture);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerProfile);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigationProfile);
        navigationView.setCheckedItem(R.id.navProfile);
        setupDrawerContent(navigationView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) return true;

        return super.onOptionsItemSelected(item);
    }


    /**
     * Handles the navigation menu Items on ProfileActivity
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
                                intent = new Intent(ProfileActivity.this, AdvertsActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.navAddAdvert:
                                intent = new Intent(ProfileActivity.this, AddAdvertActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.navProfile:
                                return false;
                            case R.id.navSettings:
                                intent = new Intent(ProfileActivity.this, SettingActivity.class);
                                startActivity(intent);
                                break;
                        }

                        return true;
                    }
                }
        );
    }


    public void getUserData(String token, String email, String userId, RequestQueue requestQueue) {
        String url = "http://school.haitamattar.com/userProfile";
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("authToken", token);
        params.put("userId", userId);

        Log.d("userd", userId);

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Gson gson = new Gson();
                    User user = gson.fromJson(String.valueOf(response.getJSONObject("User")), User.class);
                    userInfo = user;
                    name.setText(user.getName());
                    bio.setText(user.getBio());

                } catch (Exception e) {
                    Log.d("Foutie ", e + "Std");
                }

            }
        }, new Response.ErrorListener() {

//            success.setMessage("Wrong email or password");
//            success.setPositiveButton("Try again", new DialogInterface.OnClickListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Errordfasdf : ", error.toString());
                // Show alert box after wrong creditentials or other errors
                AlertDialog.Builder wrongCredit = new AlertDialog.Builder(ProfileActivity.this);
                wrongCredit.setMessage("Can't get user details");

                wrongCredit.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });
                wrongCredit.show();

            }
        });

        requestQueue.add(request_json);

    }
}
