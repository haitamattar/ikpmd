package com.school.haitamelattar.freeob;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.lang.reflect.Array;

public class AdvertsActivity extends AppCompatActivity {

    private ListView advertsListView;
    private RequestQueue requestQueue;
    private final String getUrl = "http://192.168.1.233:8888/adverts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adverts);
        final String[] test = new String[5];

        advertsListView = (ListView)findViewById(R.id.advertsListView);
        StringRequest request = new StringRequest(getUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CODE", response);

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

}
