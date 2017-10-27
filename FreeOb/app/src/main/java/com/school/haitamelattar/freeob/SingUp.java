package com.school.haitamelattar.freeob;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import static com.school.haitamelattar.freeob.R.id.editNameText;

public class SingUp extends AppCompatActivity {

    public User currentUser;
    Button registerBtn;
    EditText emailRegister, nameRegister, passwordRegister;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        registerBtn = (Button) findViewById(R.id.singUpBtn);
        // Post params to be sent to the server
        nameRegister = (EditText) findViewById(editNameText);
        emailRegister = (EditText) findViewById(R.id.emailEditTExt);
        passwordRegister = (EditText) findViewById(R.id.editPassword);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/AvenirNextLTPro-Regular.ttf");
        registerBtn.setTypeface(typeface);
        nameRegister.setTypeface(typeface);
        emailRegister.setTypeface(typeface);
        passwordRegister.setTypeface(typeface);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameRegister.getText().toString().length() <= 0) {
                    nameRegister.setError("Enter your name");
                } else if (emailRegister.getText().toString().length() <= 0) {
                    emailRegister.setError("Enter your email");
                } else if (passwordRegister.getText().toString().length() <= 0) {
                    passwordRegister.setError("Enter your password");
                } else {
                    singUpAndLogin(nameRegister.getText().toString(),
                                   emailRegister.getText().toString(),
                                   passwordRegister.getText().toString(),requestQueue);
                }
            }
        });
    }

    public void singUpAndLogin(String name, String email, String password, RequestQueue requestQueue) {
        String url = "http://school.haitamattar.com/register";
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("email", email);
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
                    settings.edit().putString("id", currentUser.getId().toString()).commit();
                    Intent advertsIntent = new Intent(SingUp.this, AdvertsActivity.class);
                    startActivity(advertsIntent);
                } catch (Exception e) {
//                    Log.d("Fout kan email ligt ", e + "Std");
                }

            }
        }, new Response.ErrorListener() {

//            success.setMessage("Wrong email or password");
//            success.setPositiveButton("Try again", new DialogInterface.OnClickListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: \n\n", error.toString());
                // Show alert box after wrong creditentials or other errors
                AlertDialog.Builder wrongCredit = new AlertDialog.Builder(SingUp.this);
                wrongCredit.setMessage("Email already exists");

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
