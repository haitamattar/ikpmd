package com.school.haitamelattar.freeob;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.school.haitamelattar.freeob.model.Advert;
import com.school.haitamelattar.freeob.model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;

public class AdvertDetailActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_detail);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerAdvertDetail);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigationAdvertDetail);
        navigationView.setCheckedItem(R.id.navAdverts);
        setupDrawerContent(navigationView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Advert advert = (Advert) getIntent().getSerializableExtra("Advert");

        ImageView imageView = (ImageView) this.findViewById(R.id.advertDetailImage);
        // Image for testing
        Picasso.with(this).load("http://www.studentfiets.nl/wp-content/uploads/2015/04/herenfiets-03.jpg").into(imageView);

        TextView categoryTextView = (TextView) this.findViewById(R.id.advertDetailCategory);
        TextView titleTextView = (TextView) this.findViewById(R.id.advertDetailTitle);
        TextView descriptionTextView = (TextView) this.findViewById(R.id.advertDetailDescription);

        categoryTextView.setText(advert.getCategoryName());
        titleTextView.setText(advert.getName());
        descriptionTextView.setText(advert.getDescription());

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        // User details
        SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String userid = settings.getString("id", "");

        visitAdvert(userid, advert.getId(),requestQueue);
    }

    public void expandText(View view) {
        TextView textView = (TextView) this.findViewById(R.id.advertDetailDescription);
        textView.setMaxLines(Integer.MAX_VALUE);
        textView.setEllipsize(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) return true;

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles the navigation menu Items on AdvertDetailActivity
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
                                intent = new Intent(AdvertDetailActivity.this, AdvertsActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.navAddAdvert:
                                intent = new Intent(AdvertDetailActivity.this, AddAdvertActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.navProfile:
                                intent = new Intent(AdvertDetailActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.navSettings:
                                intent = new Intent(AdvertDetailActivity.this, SettingActivity.class);
                                startActivity(intent);
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    // Visit advert
    public void visitAdvert(String userId, String advertId, RequestQueue requestQueue) {
        String url = "http://192.168.1.36:8888/visitAdvert";
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);
        params.put("advertId", advertId);

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Count advert", response.toString());
                } catch (Exception e) {
                    Log.d("Fout geen visitor maken", e + "Std");
                }

            }
        }, new Response.ErrorListener() {

//            success.setMessage("Wrong email or password");
//            success.setPositiveButton("Try again", new DialogInterface.OnClickListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: \n\n", error.toString());
            }
        });

        requestQueue.add(request_json);

    }


}
