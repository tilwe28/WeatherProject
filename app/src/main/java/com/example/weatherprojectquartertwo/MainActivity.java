package com.example.weatherprojectquartertwo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
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
import java.util.ArrayList;
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

    //Declaring widgets
    TextView tv_currentWeather, tv_citiesInCircle, tv_cityOne, tv_cityTwo, tv_cityThree, tv_temperatureOne, tv_temperatureTwo, tv_temperatureThree, tv_timeOne, tv_timeTwo, tv_timeThree, tv_dateOne, tv_dateTwo, tv_dateThree, tv_conditionOne, tv_conditionTwo, tv_conditionThree;
    EditText et_latitude, et_longitude;
    View view_daynight, view_one, view_two, view_three;
    ImageView iv_one, iv_two, iv_three;
    Button button_getWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assigning widgets
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
        view_daynight = findViewById(R.id.view_daynight);
        view_one = findViewById(R.id.view_ivOne);
        view_two = findViewById(R.id.view_ivTwo);
        view_three = findViewById(R.id.view_ivThree);

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

                //determing time and day or night, and changing look of UI
                //each city will be about the same so only 1 is needed to determine day or night
                ArrayList<Integer> timesOne = timesList((cityOne.getLong("dt")));
                if (timesOne.get(2)<7 || timesOne.get(2)>=18) {
                    view_daynight.setBackground(getDrawable(R.drawable.background_night));
                    tv_cityOne.setTextColor(Color.WHITE);
                    tv_cityTwo.setTextColor(Color.WHITE);
                    tv_cityThree.setTextColor(Color.WHITE);
                    tv_conditionOne.setTextColor(Color.WHITE);
                    tv_conditionTwo.setTextColor(Color.WHITE);
                    tv_conditionThree.setTextColor(Color.WHITE);
                    tv_temperatureOne.setTextColor(Color.WHITE);
                    tv_temperatureTwo.setTextColor(Color.WHITE);
                    tv_temperatureThree.setTextColor(Color.WHITE);
                    tv_timeOne.setTextColor(Color.WHITE);
                    tv_timeTwo.setTextColor(Color.WHITE);
                    tv_timeThree.setTextColor(Color.WHITE);
                    tv_dateOne.setTextColor(Color.WHITE);
                    tv_dateTwo.setTextColor(Color.WHITE);
                    tv_dateThree.setTextColor(Color.WHITE);
                } else if (timesOne.get(2)>= 7 && timesOne.get(2)<18) {
                    view_daynight.setBackground(getDrawable(R.drawable.background_day));
                }//changes UI to day or night theme
                //Log.d("TAG", ""+cityOne.getLong("dt"));

                //setting the date textviews
                ArrayList<Integer> timesTwo = timesList((cityTwo.getLong("dt")));
                ArrayList<Integer> timesThree = timesList((cityThree.getLong("dt")));
                tv_dateOne.setText(timesOne.get(0)+"/"+(timesOne.get(1)+1)+"/"+"21");
                tv_dateTwo.setText(timesTwo.get(0)+"/"+(timesTwo.get(1)+1)+"/"+"21");
                tv_dateThree.setText(timesThree.get(0)+"/"+(timesThree.get(1)+1)+"/"+"21");

                //setting the time textviews
                setTime(timesOne, tv_timeOne);
                setTime(timesTwo, tv_timeTwo);
                setTime(timesThree, tv_timeThree);

                //setting the city name textviews
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
                setCondition(conditionTwo, iv_two, tv_conditionTwo);
                setCondition(conditionThree, iv_three, tv_conditionThree);

            }//trying to display weather information to the ui
            catch (JSONException e) {
                e.printStackTrace();
            }//catches any errors
            //Log.d("TAG", information.toString());
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

    public ArrayList<Integer> timesList(long epochTime) {

        ArrayList<Integer> times = new ArrayList<>();

        //this time isn't the current time but the time that correlates to when this weather data is for (it will be very recent)
        epochTime = epochTime - 1609477200;//seconds since the start of 2021 in EST
        int days = (int) (epochTime/86400);//days that have passed (it should be minus 1 of the date like if it it's Jan 19, then 18 days passed)
        int seconds = (int) (epochTime - (days*86400));//seconds that have passed in the current day
        int hour = (seconds/3600);//hours that have passed in current day
        seconds = seconds - (hour*3600);//seconds within the hour
        int minutes = seconds/60;//minutes in hour
        int month = ((days+1)/31)+1;//month of the year
        Log.d("TAG", ""+epochTime);
        Log.d("TAG", ""+month);
        Log.d("TAG", ""+days);
        Log.d("TAG", ""+hour);
        Log.d("TAG", ""+minutes);
        Log.d("TAG", ""+seconds);

        times.add(0, month);
        times.add(1, days);
        times.add(2, hour);
        times.add(3, minutes);
        times.add(4, seconds);

        return times;
    }//cityTime

    public void setTime(ArrayList<Integer> times, TextView tv) {
        String ampm = "AM";
        if (times.get(2)>=12) {
            ampm = "PM";
            if (times.get(2)>12)
                times.set(2, times.get(2)-12);
        }
        if (times.get(3)<10)
            tv.setText(times.get(2)+":0"+times.get(3)+" "+ampm+" EST");
        else tv.setText(times.get(2)+":"+times.get(3)+" "+ampm+" EST");
    }//setTime

}//MainActivity
