package com.example.cet191.soundstageapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

/*
* Created by Phil on 2/9/2016
*
* */
public class LumensActivity extends LumensActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lumens);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void addListenerOnButton() {
        ImageButton mainButton = (ImageButton) findViewById(R.id.imageButton3);

        mainButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }

        });
    }

    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            final float currentReading = event.values[0];

            int reading = (int) currentReading;


            //addLightReading(reading);
        }
    }

    public void onClick(View v) {

        Intent intent = new Intent(v.getContext(), MainActivity.class);
        startActivityForResult(intent, 0);
    }
}


