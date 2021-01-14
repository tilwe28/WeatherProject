package com.example.weatherprojectquartertwo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.*;

public class MainActivity extends AppCompatActivity {

    String apiKey = "cfa7d5a5f5ac95c413ab1ba1b48ecfe9";
    Double lon=-74.5607, lat=40.4209;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        URLTask urlTask = new URLTask();
        urlTask.execute();

    }//onCreate


    private class URLTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL("http", "api.openweathermap.org", "//data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("TAG", url.toString());

            URLConnection urlConnection = null;
            try {
                urlConnection = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Log.d("TAG", urlConnection.toString());

            InputStream inputStream = null;
            try {
                inputStream = urlConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Log.d("TAG", inputStream.toString());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            JSONObject information = new JSONObject();
            try {
                information.put("weather", bufferedReader.readLine());
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            Log.d("TAG", information.toString());

            return null;
        }
    }

}//MainActivity
