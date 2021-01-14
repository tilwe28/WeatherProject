package com.example.weatherprojectquartertwo;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {

    /*
     Current weather calls
     *api.openweathermap.org/data/2.5/weather?zip={zip code},{country code}&appid={API key} zip code
     *api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key} coordinates

     Forecast calls: change weather to forecast/daily
     */

    String apiKey = "cfa7d5a5f5ac95c413ab1ba1b48ecfe9";
    Double lon=-74.5607, lat=40.4209;

    URL url = null;
    URLConnection urlConnection = null;
    InputStream inputStream = null;
    JSONObject information = new JSONObject();

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
            try {
                url = new URL("http", "api.openweathermap.org", "//data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("TAG", url.toString());

            try {
                urlConnection = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Log.d("TAG", urlConnection.toString());

            try {
                inputStream = urlConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Log.d("TAG", inputStream.toString());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            try {
                information.put("", bufferedReader.readLine());
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            Log.d("TAG", information.toString());

            return null;
        }
    }

}//MainActivity
