package com.school.haitamelattar.freeob;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.school.haitamelattar.freeob.json.GetJsonData;
import com.school.haitamelattar.freeob.model.Advert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetJsonData().execute("http://192.168.1.36:8888/adverts");
//        Log.e("JSON OB: " , new GetJsonData().execute("http://192.168.1.36:8888/adverts").toString());

        Gson gson = new Gson();
        String jsonOutput = new GetJsonData().execute("http://192.168.1.36:8888/adverts").toString();
        Log.d("SHIT", jsonOutput);

        Advert[] enums = gson.fromJson(jsonOutput, Advert[].class);

        for(int i = 0; i < enums.length; i++){
            Log.e("name: " , enums[i].getClass().getName());
        }


    }
}
