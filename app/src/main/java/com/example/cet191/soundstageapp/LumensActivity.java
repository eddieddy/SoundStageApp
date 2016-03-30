package com.example.cet191.soundstageapp;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
* Created by Phil on 2/9/2016
*
* */
public class LumensActivity extends LumensActivityBase {

    private static final String TAG = "LumensActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lumens);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        min = (TextView) findViewById(R.id.txtLumensMin);
        max = (TextView) findViewById(R.id.txtLumensMax);
        avg = (TextView) findViewById(R.id.txtLumensAvg);
        lumensmeter = (Speedometer) findViewById(R.id.Lumensmeter);

        Button btnResetLumens = (Button)findViewById(R.id.btnResetLumens);

        btnResetLumens.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resetLightReadings();
            }
        });
    }

//    public void onSensorChanged(SensorEvent event) {
//        // TODO Auto-generated method stub
//        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
//            final float currentReading = event.values[0];
//
//            int reading = (int) currentReading;
//
//
//            //addLightReading(reading);
//        }
//    }
/*
    public void onClick(View v) {

        Intent intent = new Intent(v.getContext(), MainActivity.class);
        startActivityForResult(intent, 0);
    }*/

}


