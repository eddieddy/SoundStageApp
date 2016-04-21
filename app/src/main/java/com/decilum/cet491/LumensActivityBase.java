package com.decilum.cet491;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/***
 * Created by Eddie on 2/24/2016.
 */
public class LumensActivityBase extends ActivityBaseClass {
    private List<Integer> _lightReadings;
    SensorManager sensorManager;
    Sensor lightSensor;
    Speedometer lumensmeter;
    TextView min;
    TextView avg;
    TextView max;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private float lumensMeterMinSize = 650;

    protected List<Integer> getLightReadings() {
        return _lightReadings;
    }

    protected void resetLightReadings() {

        _lightReadings.clear();
        lumensmeter.setMaxSize(lumensMeterMinSize);
    }

    protected int getMaxLightReading() {
        return Collections.max(getLightReadings());
    }

    protected int getMinLightReading() {
        return Collections.min(getLightReadings());
    }

    protected int getAverageLightReading() {
        int sum = 0;
        for (float value : getLightReadings()) {
            sum += value;
        }

        return sum / _lightReadings.size();
    }

    private void addLightReading(int reading) {
        _lightReadings.add(reading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _lightReadings = new ArrayList<Integer>();

        sensorManager
                = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor
                = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void startLightSensorReading()
    {
        if (lightSensor == null) {
            Toast.makeText(this,
                    "No Light Sensor detected on your device!",
                    Toast.LENGTH_LONG).show();
        } else {
            float max = lightSensor.getMaximumRange();

            sensorManager.registerListener(lightSensorEventListener,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    private void stopLightSensor()
    {
        if(sensorManager != null && lightSensor != null)
        {
            sensorManager.unregisterListener(lightSensorEventListener, lightSensor);
        }
    }

    SensorEventListener lightSensorEventListener
            = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                float currentReading = event.values[0];

                // Get our reading and add it to the list.
                int reading = (int) currentReading;
                addLightReading(reading);

                // set the avg reading.
                String minValue = Integer.toString(getMinLightReading());
                min.setText(String.format("Min: %s", minValue));

                // set the min reading.
                String avgValue = Integer.toString(getAverageLightReading());
                avg.setText(String.format("Avg: %s", minValue));;

                // set the max reading.
                String maxValue = Integer.toString(getMaxLightReading());
                max.setText(String.format("Max: %s", minValue));

                float getCurrentMaxSize = lumensmeter.getMaxSize();
                if(currentReading > getCurrentMaxSize || currentReading/4 < getCurrentMaxSize)
                {
                    calculateMaxSize(currentReading);
                }

                // Update the lumens graph.
                lumensmeter.setCurrentSpeed(currentReading);
                if (BuildConfig.DEBUG) {
//                    Log.d(getLocalClassName(), String.format("Light readings: current: %s, min: %s, max: %s, avg: %s", currentReading, minValue, maxValue, avgValue));
                }
            }
        }
    };

    private void calculateMaxSize(float currentReading)
    {
        if(currentReading < lumensMeterMinSize)
            lumensmeter.setMaxSize(lumensMeterMinSize);
        else if(currentReading < 10000)
            lumensmeter.setMaxSize(10000);
        else if(currentReading < 20000)
            lumensmeter.setMaxSize(20000);
        else if(currentReading < 30000)
            lumensmeter.setMaxSize(30000);
        else if(currentReading < 40000)
            lumensmeter.setMaxSize(40000);
        else if(currentReading < 50000)
            lumensmeter.setMaxSize(500000);
        else if(currentReading < 60000)
            lumensmeter.setMaxSize(60000);
        else if(currentReading < 70000)
            lumensmeter.setMaxSize(70000);
        else if(currentReading < 80000)
            lumensmeter.setMaxSize(80000);
        else if(currentReading < 90000)
            lumensmeter.setMaxSize(90000);
        else if(currentReading < 100000)
            lumensmeter.setMaxSize(100000);
    }

    @Override
    protected void onStart() {
        super.onStart();

        startLightSensorReading();
   }

    @Override
    protected void onStop() {
        super.onStop();

        stopLightSensor();
    }
}
