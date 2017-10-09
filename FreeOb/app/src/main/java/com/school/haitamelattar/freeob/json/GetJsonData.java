package com.school.haitamelattar.freeob.json;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by haitamelattar on 08-10-17.
 */

public class GetJsonData extends AsyncTask<String, String > {

    @Override
    protected List<Course> doInBackground(String... params) {
        HttpURLConnection connection = null;

        BufferedReader reader = null;

        URL url = null;
        try {
            url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = null;
            stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            String finalJson = buffer.toString();
            System.out.println("JSON: " + finalJson);
            Gson gson = new Gson();
            JSONObject parentObject = new JSONObject(finalJson);

            List<Course> data = (List<Course>) gson.fromJson(parentObject.toString(), Course.class);

            return data;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("FOUT");
        return null;
    }

}
