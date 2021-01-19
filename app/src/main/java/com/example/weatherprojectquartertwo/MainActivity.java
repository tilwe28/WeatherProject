package com.example.weatherprojectquartertwo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;

import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    /*
     *Current weather calls
     *api.openweathermap.org/data/2.5/weather?zip={zip code},{country code}&appid={API key} zip code
     *api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key} coordinates
     *api.openweathermap.org/data/2.5/find?lat={lat}&lon={lon}&cnt={cnt}&appid={API key} circle around coordinates
     *
     *Forecast calls: change weather to forecast/daily
     */

    String apiKey = "cfa7d5a5f5ac95c413ab1ba1b48ecfe9", lat, lon;//40.4209, -74.5607 Kendall Park

    TextView tv_currentWeather, tv_citiesInCircle, tv_daynight, tv_cityOne, tv_cityTwo, tv_cityThree, tv_temperatureOne, tv_temperatureTwo, tv_temperatureThree, tv_timeOne, tv_timeTwo, tv_timeThree, tv_dateOne, tv_dateTwo, tv_dateThree, tv_conditionOne, tv_conditionTwo, tv_conditionThree;
    EditText et_latitude, et_longitude;
    ImageView iv_one, iv_two, iv_three;
    Button button_getWeather;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_currentWeather = findViewById(R.id.textView_currentWeather);
        tv_citiesInCircle = findViewById(R.id.textView_citiesInCircle);
        tv_daynight = findViewById(R.id.textView_daynight);
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
        view = findViewById(R.id.view);

        iv_one.setImageResource(R.drawable.squareoutline);
        iv_two.setImageResource(R.drawable.squareoutline);
        iv_three.setImageResource(R.drawable.squareoutline);

        et_latitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_latitude.getText().toString().equals("latitude"))
                    et_latitude.setText("");
            }
        });
        et_latitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lat = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                lat = s.toString();
            }
        });
        et_longitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_longitude.getText().toString().equals("longitude"))
                    et_longitude.setText("");
            }
        });
        et_longitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lon = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                lon = s.toString();
            }
        });

        button_getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URLTask urlTask = new URLTask(lat, lon);
                urlTask.execute();
            }
        });
    }//onCreate

    private class URLTask extends AsyncTask<String, String, String> {

        private String latitude, longitude;

        public URLTask(String latitude, String longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "";

            URL url = null;
            URLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                url = new URL("http", "api.openweathermap.org", "/data/2.5/find?lat=" + latitude + "&lon=" + longitude + "&cnt=3"+ "&appid=" + apiKey);
                urlConnection = url.openConnection();
                inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                int jsonChar = bufferedReader.read();
                while (jsonChar != -1) {
                    json += (char) jsonChar;
                    jsonChar = bufferedReader.read();
                }
            }//trying to get api call from url and making it into a readable string to be used for a jsonObject
            catch (IOException e) {
                e.printStackTrace();
            }//catches any errors

            Log.d("TAG", url.toString());
            //Log.d("TAG", json);

            return json;
        }//doInBackground

        @Override
        protected void onPostExecute(String s) {

            //Log.d("TAG", s);
            JSONObject information = null;
            JSONObject cityOne = new JSONObject();
            JSONObject cityTwo = new JSONObject();
            JSONObject cityThree = new JSONObject();
            try {
                //setting the JSONObject's for each city
                information = new JSONObject(s);
                cityOne = information.getJSONArray("list").getJSONObject(0);
                cityTwo = information.getJSONArray("list").getJSONObject(1);
                cityThree = information.getJSONArray("list").getJSONObject(2);

                //setting the city name textview's
                tv_cityOne.setText(cityOne.getString("name"));
                tv_cityTwo.setText(cityTwo.getString("name"));
                tv_cityThree.setText(cityThree.getString("name"));

                //setting the temperature textview's after doing required calculations
                Double temperatureOne = (int)(((cityOne.getJSONObject("main").getDouble("temp")-273.15)*(9.0/5.0)+32.0)*10)/10.0;
                Double temperatureTwo = (int)(((cityTwo.getJSONObject("main").getDouble("temp")-273.15)*(9.0/5.0)+32.0)*10)/10.0;
                Double temperatureThree = (int)(((cityThree.getJSONObject("main").getDouble("temp")-273.15)*(9.0/5.0)+32.0)*10)/10.0;
                tv_temperatureOne.setText(temperatureOne+"°");
                tv_temperatureTwo.setText(temperatureTwo+"°");
                tv_temperatureThree.setText(temperatureThree+"°");

                //setting the imageview with the correct weather icon and the weather condition textview
                JSONObject conditionOne = cityOne.getJSONArray("weather").getJSONObject(0);
                JSONObject conditionTwo = cityTwo.getJSONArray("weather").getJSONObject(0);
                JSONObject conditionThree = cityThree.getJSONArray("weather").getJSONObject(0);
                setCondition(conditionOne, iv_one, tv_conditionOne);
                setCondition(conditionOne, iv_two, tv_conditionTwo);
                setCondition(conditionOne, iv_three, tv_conditionThree);

                /*
                long epochTime = cityOne.getLong("dt");
                Date date = new Date(epochTime*1000L);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                String formattedDate = dateFormat.format(date);
                Log.d("TAG", ""+epochTime);
                Log.d("TAG", formattedDate);
                 */

            }//trying to display weather information to the ui
            catch (JSONException e) {
                e.printStackTrace();
            }//catches any errors
            Log.d("TAG", information.toString());
            Log.d("TAG", cityOne.toString());
            Log.d("TAG", cityTwo.toString());
            Log.d("TAG", cityThree.toString());

        }//onPostExecute
    }//URLTask

    public void setCondition(JSONObject condition, ImageView iv, TextView tv) {
        try {
            tv.setText(condition.getString("description"));//sets weather condition textviews

            switch (condition.getString("main")) {
                case "Clear":
                    iv.setImageResource(R.drawable.sunny);
                    break;
                case "Clouds":
                    iv.setImageResource(R.drawable.cloudy);
                    break;
                case "Rain":
                case "Drizzle":
                    iv.setImageResource(R.drawable.rainy);
                    break;
                case "Snow":
                    iv.setImageResource(R.drawable.snowy);
                    break;
                case "Thunderstorm":
                    iv.setImageResource(R.drawable.stormy);
                    break;
                default:
                    iv.setImageResource(R.drawable.squareoutline);
                    break;
            }//switch statement checks weather condition and sets image accordingly
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }//setCondition

}//MainActivity
