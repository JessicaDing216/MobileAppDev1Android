package com.example.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ForecastView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_view);
        //getting the intent
        Intent intent=getIntent();
        String cityName=intent.getStringExtra("CITY_NAME");
        TextView forecastCityTextView = findViewById(R.id.CityTextView);

        if (cityName!=null){
            forecastCityTextView.setText(cityName);
        }
        else{
            forecastCityTextView.setText(R.string.no_location);
        }
    }

    public void ReturnMain(View view) {
//        //switch to another activity
//        Intent returnMain = new Intent(this, MainActivity.class);
//        startActivity(returnMain);
        //to resume to main with and still keeping the bundle data, basically just kill this activity and return to the previous
        finish();
    }
}