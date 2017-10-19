package com.school.haitamelattar.freeob;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.school.haitamelattar.freeob.model.User;

import org.json.JSONObject;

import java.util.HashMap;

public class SplashScreen extends AppCompatActivity {

    RequestQueue requestQueue;
    public User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread myThread = new Thread(){
            @Override
            public void run(){
                try{
                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                    // Check if user has an account, if so go to all adverts activity
                    SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                    String token = settings.getString("loginToken", "");
                    String email = settings.getString("email", "");
                    sleep(1000);
                    verifyToken(token, email, requestQueue);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }

    // Verify token
    public void verifyToken(String token, String email, RequestQueue requestQueue) {
        String url = "http://192.168.1.36:8888/tokenCheck";
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("authToken", token);

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Test TOKEN", "TTT");
                try {
                    Gson gson = new Gson();
                    User user = gson.fromJson(String.valueOf(response), User.class);
                    currentUser = user;
                    Intent advertsIntent = new Intent(SplashScreen.this, AdvertsActivity.class);
                    startActivity(advertsIntent);
                } catch (Exception e) {
                    // Log.d("exc", e + " ");
                    // DELETE invalid token just to make shure
                    SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                    settings.edit().remove("loginToken").commit();
                    settings.edit().remove("email").commit();
                    Intent loginPage = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(loginPage);
                }

            }
        }, new Response.ErrorListener() {

//            success.setMessage("Wrong email or password");
//            success.setPositiveButton("Try again", new DialogInterface.OnClickListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: \n\n", error.toString());
                // DELETE invalid token just to make shure
                SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                settings.edit().remove("loginToken").commit();
                settings.edit().remove("email").commit();
                Intent loginPage = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(loginPage);
            }
        });

        requestQueue.add(request_json);

    }
}
