package com.school.haitamelattar.freeob;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.school.haitamelattar.freeob.model.Category;
import com.school.haitamelattar.freeob.view.CategoryAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddAdvertActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private Spinner categorySpinner;
    private Button saveBtn;
    private EditText titleText, descText;
    private CategoryAdapter adapter;
    private final String getCategoryUrl = "http://192.168.1.233:8888/categories";
    private final String postNewAdvert = "http://192.168.1.233:8888/addAdvert";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advert);

        // Setting for navigation menu
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerAddAdvert);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigationAddAdvert);
        navigationView.setCheckedItem(R.id.navAddAdvert);
        setupDrawerContent(navigationView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // End settings for navigation menu

        saveBtn = (Button) findViewById(R.id.AddAdvertNextStep);
        titleText = (EditText) findViewById(R.id.addAdvertTitleText);
        descText = (EditText) findViewById(R.id.addAdvertDescText);
        categorySpinner = (Spinner) findViewById(R.id.addAdvertCategorySpin);

        // Fill the categorySpinner (dropdown) with category data
        StringRequest request = new StringRequest(getCategoryUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                final ArrayList<Category> categories = gson.fromJson(response,
                        new TypeToken<List<Category>>(){}.getType());

                Log.d("ADVERT", categories.get(0).toString());
                adapter = new CategoryAdapter(AddAdvertActivity.this,
                        R.layout.content_category_spinner, R.id.spinnerItem, categories);

                categorySpinner.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddAdvertActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

        saveBtn.setOnClickListener((new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // The selected item of the spinner
                //View selectedView = categorySpinner.getSelectedView();

                //Log.d("ITEM", categorySpinner.get);

                if (titleText.getText().toString().length() <= 0) {
                    titleText.setError("Enter a title");
                } else if (descText.getText().toString().length() <= 0) {
                    descText.setError("Enter a description");
//                } else if ( == 0) {
//                    adapter.setError(selectedView, "Choose a category");
                } else {
                    addAdvert(
                            titleText.getText().toString(),
                            descText.getText().toString(),
                            "9"
                            //selectedView.getTag().toString()
                    );
                }
            }
        }));
    }

    public void addAdvert(String title, String description, String categoryId) {
        SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String token = settings.getString("loginToken", "");
        String email = settings.getString("email", "");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("authToken", token);
        params.put("advert_name", title);
        params.put("description", description);
        params.put("category", categoryId);

        // Fill the categorySpinner (dropdown) with category data
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postNewAdvert, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddAdvertActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) return true;

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles the navigation menu Items on AddAdvertActivity
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
                                intent = new Intent(AddAdvertActivity.this, AdvertsActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.navAddAdvert:
                                return false;
                            case R.id.navProfile:
                                intent = new Intent(AddAdvertActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.navSettings:
                                intent = new Intent(AddAdvertActivity.this, SettingActivity.class);
                                startActivity(intent);
                                break;
                        }
                        return true;
                    }
                }
        );
    }
}
