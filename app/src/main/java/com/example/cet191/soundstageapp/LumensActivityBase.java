package com.example.cet191.soundstageapp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Eddie on 2/24/2016.
 */
public class LumensActivityBase extends ActivityBaseClass {
    private List<Integer> _lightReadings;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected List<Integer> getLightReadings() {
        return _lightReadings;
    }

    protected void resetLightReadings() {
        _lightReadings.clear();
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

        SensorManager sensorManager
                = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor
                = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) {
            Toast.makeText(this,
                    "No Light Sensor detected on your device!",
                    Toast.LENGTH_LONG).show();
        } else {
            float max = lightSensor.getMaximumRange();

            sensorManager.registerListener(lightSensorEventListener,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
                final float currentReading = event.values[0];

                // Get our reading and add it to the list.
                int reading = (int) currentReading;
                addLightReading(reading);

                // set the avg reading.
                TextView min = (TextView) findViewById(R.id.txtLumensMin);
                min.setText(Integer.toString(getMinLightReading()));

                // set the min reading.
                TextView avg = (TextView) findViewById(R.id.txtLumensAvg);
                avg.setText(Integer.toString(getAverageLightReading()));

                // set the max reading.
                TextView max = (TextView) findViewById(R.id.txtLumensMax);
                max.setText(Integer.toString(getMaxLightReading()));
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LumensActivityBase Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.cet191.soundstageapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LumensActivityBase Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.cet191.soundstageapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
