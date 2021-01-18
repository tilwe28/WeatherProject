package com.example.weatherprojectquartertwo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
     *Current weather calls
     *api.openweathermap.org/data/2.5/weather?zip={zip code},{country code}&appid={API key} zip code
     *api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key} coordinates
     *api.openweathermap.org/data/2.5/find?lat={lat}&lon={lon}&cnt={cnt}&appid={API key} circle around coordinates
     *
     *Forecast calls: change weather to forecast/daily
     */

    String apiKey = "cfa7d5a5f5ac95c413ab1ba1b48ecfe9";
    Double lon, lat;//-74.5607, 40.4209;

    TextView tv_currentWeather, tv_citiesInCircle, tv_cityOne, tv_cityTwo, tv_cityThree, tv_temperatureOne, tv_temperatureTwo, tv_temperatureThree, tv_timeOne, tv_timeTwo, tv_timeThree, tv_dateOne, tv_dateTwo, tv_dateThree, tv_conditionOne, tv_conditionTwo, tv_conditionThree;
    EditText et_latitude, et_longitude;
    ImageView iv_one, iv_two, iv_three;
    Button button_getWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_currentWeather = findViewById(R.id.textView_currentWeather);
        tv_citiesInCircle = findViewById(R.id.textView_citiesInCircle);
        tv_cityOne = findViewById(R.id.textView_cityOne);
        tv_cityTwo = findViewById(R.id.textView_cityTwo);
        tv_cityThree = findViewById(R.id.textView_cityThree);
        tv_temperatureOne = findViewById(R.id.textView_temperature_cityOne);
        tv_temperatureTwo = findViewById(R.id.textView_temperature_cityTwo);
        tv_temperatureThree = findViewById(R.id.textView_temperature_cityThree);
        tv_timeOne = findViewById(R.id.textView_time_cityOne);
        tv_timeTwo = findViewById(R.id.textView_time_cityTwo);
        tv_timeThree = findViewById(R.id.textView_time_cityThree);
        tv_dateOne = findViewById(R.id.textView_date_cityOne);
        tv_dateTwo = findViewById(R.id.textView_date_cityTwo);
        tv_dateThree = findViewById(R.id.textView_date_cityThree);
        tv_conditionOne = findViewById(R.id.textView_condition_cityOne);
        tv_conditionTwo = findViewById(R.id.textView_condition_cityTwo);
        tv_conditionThree = findViewById(R.id.textView_condition_cityThree);
        et_latitude = findViewById(R.id.editText_latitude);
        et_longitude = findViewById(R.id.editText_longitutde);
        iv_one = findViewById(R.id.imageView_cityOne);
        iv_two = findViewById(R.id.imageView_cityTwo);
        iv_three = findViewById(R.id.imageView_cityThree);
        button_getWeather = findViewById(R.id.button_getWeather);

        iv_one.setImageResource(R.drawable.squareoutline);
        iv_two.setImageResource(R.drawable.squareoutline);
        iv_three.setImageResource(R.drawable.squareoutline);

        URLTask urlTask = new URLTask();
        urlTask.execute();

    }//onCreate

    private class URLTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String json = "";

            URL url = null;
            URLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                url = new URL("http", "api.openweathermap.org", "/data/2.5/find?lat=" + lat + "&lon=" + lon + "&cnt=3"+ "&appid=" + apiKey);
                urlConnection = url.openConnection();
                inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                int jsonChar = bufferedReader.read();
                while (jsonChar != -1) {
                    json += (char) jsonChar;
                    jsonChar = bufferedReader.read();
                }//trying to get api call from url and making it into a readable string to be used for a jsonObject
            } catch (IOException e) {
                e.printStackTrace();
            }//catching any errors

            Log.d("TAG", url.toString());
            //Log.d("TAG", json);

            return json;
        }//doInBackground

        @Override
        protected void onPostExecute(String s) {

            Log.d("TAG", s);

        }//onPostExecute
    }//URLTask
}//MainActivity
