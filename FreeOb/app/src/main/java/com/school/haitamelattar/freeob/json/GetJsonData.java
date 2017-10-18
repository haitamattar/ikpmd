package com.school.haitamelattar.freeob.json;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.school.haitamelattar.freeob.model.Advert;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by haitamelattar on 08-10-17.
 */

public class GetJsonData extends AsyncTask<String, String, JSONArray> {

    public String jsonString;

    @Override
    protected JSONArray doInBackground(String... params) {
        HttpURLConnection connection = null;

        BufferedReader reader = null;

        String urlString = params[0];
        URLConnection urlConn = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(urlString);
            urlConn = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
//            JSONObject goei = new JSONObject(stringBuffer.toString());
            this.jsonString = stringBuffer.toString();
            JSONArray jsonAr = new JSONArray(stringBuffer.toString());
            Log.d("DEB: ", String.valueOf(jsonAr));
            return jsonAr;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("FOUT", "WERKT NIET");
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("FOUT", "WERKT NIET");
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onPostExecute(JSONArray response) {
        if (response != null) {
            Gson gson = new Gson();
            Advert[] enums = gson.fromJson(String.valueOf(response), Advert[].class);
            for (int i = 0; i < enums.length; i++) {
                Log.d("name: ", enums[i].getName());
            }
//            adverts = enums;
            Log.e("App", "Success: ");

        }
    }

}