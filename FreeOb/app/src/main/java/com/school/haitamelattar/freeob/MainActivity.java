package com.school.haitamelattar.freeob;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.school.haitamelattar.freeob.json.GetJsonData;
import com.school.haitamelattar.freeob.model.Advert;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public Advert[] adverts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetJsonData data = new GetJsonData();
        data.execute("http://192.168.1.36:8888/auth");

//        Log.e("JSON OB: " , new GetJsonData().execute("http://192.168.1.36:8888/adverts").toString());

//        Gson gson = new Gson();
//        new GetJsonData().execute("http://192.168.1.36:8888/adverts");
//        Log.d("SHIT", data.jsonString);

//        Advert[] enums = gson.fromJson(jsonOutput, Advert[].class);

        for (int i = 0; i < adverts.length; i++) {
            Log.d("GOname: ", adverts[i].getName());
        }


    }


}
