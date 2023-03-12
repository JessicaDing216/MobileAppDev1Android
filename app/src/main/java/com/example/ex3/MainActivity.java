package com.example.ex3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private double temperature = 10;
    private String description = "Please click Refresh!";
    private double windSpeed = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //creat new web request here in onCreate
        queue = Volley.newRequestQueue(this);
    }

    @SuppressLint("SetTextI18n")

    //automatic call back from the system
    //the us state can be saved here: description, temp, etc
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("WEATHER_DES", description);
        savedInstanceState.putDouble("TEMPS", temperature);
        savedInstanceState.putDouble("WIND_SP", windSpeed);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String lang = Locale.getDefault().getLanguage();

        if (lang.equals("ja")) {
            description = savedInstanceState.getString("WEATHER_DES", "リフレッシュしてください!");
        } else if (lang.equals("zh")) {
            description = savedInstanceState.getString("WEATHER_DES", "需重新整理！");
        } else {
            description = savedInstanceState.getString("WEATHER_DES", "Needs refreshing!");
        }
        temperature = savedInstanceState.getDouble("TEMPS", 20);
        windSpeed = savedInstanceState.getDouble("WIND_SP", 5);

        //dispaly the value that's being restored
        TextView descriptionText = findViewById(R.id.textView2);
        descriptionText.setText(description);
        TextView tempTextView = findViewById(R.id.textView3);
        tempTextView.setText(temperature + "C");
    }

    public void reFreshData(View view) {
        //different url for different localization
        String api_url_en = "https://api.openweathermap.org/data/2.5/weather?lat=61.29&lon=23.45&units=metric&appid=ec19fb4c8d87881f2d7adbe2477185db\n";
        String api_url_ja = "https://api.openweathermap.org/data/2.5/weather?lat=61.29&lon=23.45&units=metric&lang=ja&appid=ec19fb4c8d87881f2d7adbe2477185db\n";
        String api_url_tw = "https://api.openweathermap.org/data/2.5/weather?lat=61.29&lon=23.45&units=metric&lang=zh_tw&appid=ec19fb4c8d87881f2d7adbe2477185db\n";
        String url = "";
        String lang = Locale.getDefault().getLanguage();

        if (lang.equals("ja")) {
            url = api_url_ja;
        } else if (lang.equals("zh")) {
            url = api_url_tw;
        } else {
            url = api_url_en;
        }


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Display the response string.
                    //for debugging display
                    Log.d("weatherapp", response);
                    parsJSONandUpdate(response);
                }, error -> {
            Log.d("weatherapp", error.toString());
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void parsJSONandUpdate(String response) {
        try {
            JSONObject weatherResponse = new JSONObject(response);
            //var for data we want do extract
            description = weatherResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            temperature = weatherResponse.getJSONObject("main").getDouble("temp");
            windSpeed = weatherResponse.getJSONObject("wind").getDouble("speed");

            TextView descriptionText = findViewById(R.id.textView2);
            descriptionText.setText(description);
            TextView tempTextView = findViewById(R.id.textView3);
            tempTextView.setText(temperature + "C");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void openForecastActivity(View view) {
        //switch to another activity
        //store the city name first (to use the method of synchronising the city name,
        // but it can be also just use the same string in the resource file
        TextView city =findViewById(R.id.textView);
        String locationName=city.getText().toString();
        Intent openForcast = new Intent(this, ForecastView.class);
        openForcast.putExtra("CITY_NAME", locationName);
        startActivity(openForcast);
    }

    public void openBrowser(View view) {
        //setting the url
        String urlString = "https://www.google.com/";
        Uri url = Uri.parse(urlString);
        //create implicit intent
        Intent openWebPage = new Intent(Intent.ACTION_VIEW, url);
        try {
            startActivity(openWebPage);
        } catch (ActivityNotFoundException e) {
            //if it's not working, the will show activity not found
        }
    }

    public void openMap(View view) {
        String locationString = "geo:61.49,23.76";
        Uri geoLocation = Uri.parse(locationString);

        Intent openMap = new Intent(Intent.ACTION_VIEW);
        openMap.setData(geoLocation);
        try {
            startActivity(openMap);
        } catch (ActivityNotFoundException e) {
            //if it's not working, the will show activity not found
        }
    }

    public void startTimer(View view) {

        Intent alarm = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE, "Time's up!")
                .putExtra(AlarmClock.EXTRA_LENGTH, 20)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        try {
            startActivity(alarm);
        } catch (ActivityNotFoundException e) {
            //if it's not working, the will show activity not found
        }
    }
}