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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.name;

public class SettingActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private RequestQueue requestQueue;
    ArrayList<Entry> yvalues = new ArrayList<Entry>();
    ArrayList<String> xVals = new ArrayList<String>();
    PieChart pieChart;



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
        pieChart = (PieChart) findViewById(R.id.piechart);

        pieChart.setUsePercentValues(false);

//        yvalues.add(new Entry(8f, 0));
//        yvalues.add(new Entry(15f, 1));
//        yvalues.add(new Entry(12f, 2));
//        yvalues.add(new Entry(25f, 3));
//        yvalues.add(new Entry(23f, 4));
//        yvalues.add(new Entry(17f, 5));

//        PieDataSet dataSet = new PieDataSet(yvalues, "Adverts");



//        xVals.add("January");
//        xVals.add("February");
//        xVals.add("March");
//        xVals.add("April");
//        xVals.add("May");
//        xVals.add("June");

//        PieData data = new PieData(xVals, dataSet);

//        pieChart.setData(data);
//        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieChart.setDescription("Visitors of every published advert");

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        // User details
        SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String userid = settings.getString("id", "");

        visitAdvertCount(userid, requestQueue);
    }

    // Visit advert
    public void visitAdvertCount(String userId, RequestQueue requestQueue) {
        String url = "http://school.haitamattar.com/advertUserVisitors?userId="+userId;
        Log.d("userid", userId);
        JsonArrayRequest request_json = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.d("Count advert VISITORS", response.toString());

                    for(int i = 0; i < response.length(); i++){
                        JSONObject jsonObj = response.getJSONObject(i);
                        String advertName = jsonObj.getString("advert_name");
                        int advertVisitCount = jsonObj.getInt("visitors");

                        // Data to be used in a pieChart
                        // Log.d("Advert name: ", advertName + " Visitors: " + advertVisitCount);
                        yvalues.add(new Entry(advertVisitCount, i));
                        xVals.add(advertName);
                    }

                    // Init pie with data
                    PieDataSet dataSet = new PieDataSet(yvalues, "Adverts");
                    PieData data = new PieData(xVals, dataSet);
                    // Convert float to int
                    data.setValueFormatter(new ValueFormatter());
                    // Set data
                    pieChart.setData(data);
                    dataSet.setValueTextSize(10);
                    pieChart.setDrawHoleEnabled(false);
                    dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

                } catch (Exception e) {
                    Log.d("Fout geen visitor", e + "Std");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pieChart.setEnabled(false);
                Log.d("Wrong Error: \n\n", error.toString());
            }
        });

        requestQueue.add(request_json);

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

// Percent (float) to int
class ValueFormatter implements com.github.mikephil.charting.formatter.ValueFormatter{

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return "" + ((int) value);
    }
}
