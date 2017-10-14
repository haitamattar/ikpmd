package com.school.haitamelattar.freeob;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.school.haitamelattar.freeob.model.Advert;
import com.school.haitamelattar.freeob.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public Advert[] adverts;
    public User currentUser;
    Button loginBtn;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.loginBtn);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        // Post params to be sent to the server


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentUser("email", "ww", requestQueue);
            }
        });


    }

    public void getCurrentUser(String username, String password, RequestQueue requestQueue){
        String url = "http://192.168.1.36:8888/auth";
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", username);
        params.put("password", password);

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Resp: ",response.toString() + " JSONNNN");
                Log.d("SHIIEIIEIT", "SODJSIFHIASHF");
                try{
                    Log.d("KADWDS ", "adsfadsfadsf");
                    Gson gson = new Gson();
                    User user = gson.fromJson(String.valueOf(response), User.class);
                    Log.d("USERRR", user.getAuthToken() + " - " + user.getEmail() + " - " + user.getId());
                    currentUser = user;
                    Log.d("CURUSER", currentUser.getAuthToken() + " - " + currentUser.getEmail() + " - " + currentUser.getId());

                }catch(Exception e)
                {
                    Log.d("Fout kan username ligt ", "Std");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: \n\n", error.toString());
            }
        });

        requestQueue.add(request_json);

    }



}
