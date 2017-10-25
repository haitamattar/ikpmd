package com.school.haitamelattar.freeob;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONObject;

import java.util.HashMap;

import static android.R.attr.typeface;


public class MainActivity extends AppCompatActivity {

    public User currentUser;
    Button loginBtn;
    TextView singUp;
    EditText emailLogin, passwordLogin;
    RequestQueue requestQueue;
    public boolean hasAccount = false;
    public static final String LOGIN_TOKEN = "loginToken";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Check if user has an account, if so go to all adverts activity
        SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String token = settings.getString("loginToken", "");
        String email = settings.getString("email", "");
        verifyToken(token, email, requestQueue);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.singUpBtn);
        // Post params to be sent to the server
        emailLogin = (EditText) findViewById(R.id.editNameText);
        passwordLogin = (EditText) findViewById(R.id.editPassword);
        singUp = (TextView) findViewById(R.id.singUpBtn);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/AvenirNextLTPro-Regular.ttf");
        loginBtn.setTypeface(typeface);
        emailLogin.setTypeface(typeface);
        passwordLogin.setTypeface(typeface);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailLogin.getText().toString().length() <= 0) {
                    emailLogin.setError("Enter your email");
                } else if (passwordLogin.getText().toString().length() <= 0) {
                    passwordLogin.setError("Enter your password");
                } else {
                    getCurrentUser(emailLogin.getText().toString(), passwordLogin.getText().toString(),
                            requestQueue);
                }
            }
        });

    }

    public void onClickSingUp(View v) {
        Intent singUpActivity = new Intent(MainActivity.this, SingUp.class);
        startActivity(singUpActivity);
    }

    public void getCurrentUser(String username, String password, RequestQueue requestQueue) {
        String url = "http://school.haitamattar.com/auth";
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", username);
        params.put("password", password);

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Gson gson = new Gson();
                    User user = gson.fromJson(String.valueOf(response), User.class);
                    currentUser = user;
//                    Log.d("CURUSER", currentUser.getAuthToken() + " - " + currentUser.getEmail() + " - " + currentUser.getId());
                    SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                    settings.edit().putString("loginToken", currentUser.getAuthToken()).commit();
                    settings.edit().putString("email", currentUser.getEmail()).commit();
                    Intent advertsIntent = new Intent(MainActivity.this, AdvertsActivity.class);
                    startActivity(advertsIntent);
                } catch (Exception e) {
//                    Log.d("Fout kan username ligt ", e + "Std");
                }

            }
        }, new Response.ErrorListener() {

//            success.setMessage("Wrong email or password");
//            success.setPositiveButton("Try again", new DialogInterface.OnClickListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: \n\n", error.toString());
                // Show alert box after wrong creditentials or other errors
                AlertDialog.Builder wrongCredit = new AlertDialog.Builder(MainActivity.this);
                wrongCredit.setMessage("Wrong email or password");

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

    @Override
    public void onBackPressed() {
        // Do nothing, because this is the homescreen after a login
        return;
    }


    // Verify token
    public void verifyToken(String token, String email, RequestQueue requestQueue) {
        String url = "http://school.haitamattar.com/tokenCheck";
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
                    Intent advertsIntent = new Intent(MainActivity.this, AdvertsActivity.class);
                    startActivity(advertsIntent);
                } catch (Exception e) {
                    // Log.d("exc", e + " ");
                    // DELETE invalid token just to make shure
                    SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                    settings.edit().remove("loginToken").commit();
                    settings.edit().remove("email").commit();
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


            }
        });

        requestQueue.add(request_json);

    }


}
