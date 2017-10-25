package com.school.haitamelattar.freeob;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.school.haitamelattar.freeob.model.Advert;
import com.school.haitamelattar.freeob.view.AdvertsAdapter;

public class AdvertsActivity extends AppCompatActivity {

    private ListView advertsListView;
    private RequestQueue requestQueue;
    private final String getUrl = "http://school.haitamattar.com/adverts";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adverts);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setCheckedItem(R.id.navAdverts);
        setupDrawerContent(navigationView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        advertsListView = (ListView)findViewById(R.id.advertsListView);
        StringRequest request = new StringRequest(getUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                final Advert[] adverts = gson.fromJson(response, Advert[].class);

                AdvertsAdapter adapter = new AdvertsAdapter(AdvertsActivity.this, adverts);

                advertsListView.setAdapter(adapter);

                // Show the content of the ListView item after its clicked
                advertsListView.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        Advert advert = adverts[i];

                        Intent detailIntent = new Intent(AdvertsActivity.this,
                                AdvertDetailActivity.class);
                        detailIntent.putExtra("Advert", advert);

                        startActivity(detailIntent);
                    }

                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdvertsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        // Do nothing, because this is the homescreen after a login
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) return true;

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles the navigation menu Items on AdvertsActivity
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
                                return false;
                            case R.id.navAddAdvert:
                                intent = new Intent(AdvertsActivity.this, AddAdvertActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.navProfile:
                                intent = new Intent(AdvertsActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.navSettings:
                                intent = new Intent(AdvertsActivity.this, SettingActivity.class);
                                startActivity(intent);
                                break;
                        }

                        return true;
                    }
                }
        );
    }
}