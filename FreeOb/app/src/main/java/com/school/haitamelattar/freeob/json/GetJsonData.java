package com.school.haitamelattar.freeob.json;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by haitamelattar on 08-10-17.
 */

public class GetJsonData extends AsyncTask<String, String, JSONObject > {

    @Override
    protected JSONObject doInBackground(String... params) {
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
            JSONObject goei = new JSONObject(stringBuffer.toString());
            return new JSONObject(stringBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if(bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onPostExecute(JSONObject response)
    {
        if(response != null)
        {
            try {
                Log.e("App", "Success: " + response.getString("JsonElement") );
            } catch (JSONException ex) {
                Log.e("App", "Failure", ex);
            }
        }
    }

}