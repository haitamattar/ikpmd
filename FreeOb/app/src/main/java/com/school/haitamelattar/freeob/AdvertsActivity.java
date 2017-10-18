package com.school.haitamelattar.freeob;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.school.haitamelattar.freeob.model.Advert;

public class AdvertsActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private final String getUrl = "http://192.168.1.233:8888/adverts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adverts);

        StringRequest request = new StringRequest(getUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CODE", response);

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Advert[] adverts = gson.fromJson(response, Advert[].class);
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

}
