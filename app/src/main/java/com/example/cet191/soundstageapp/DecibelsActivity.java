package com.example.cet191.soundstageapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Config;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class DecibelsActivity extends ActivityBaseClass {
    private List<Double> decibelList = new ArrayList<Double>();;

    private Double getDecibelMin()
    {
        return Collections.min(decibelList);
    }
    private Double getDecibelMax()
    {
        return Collections.max(decibelList);
    }
    private Double getDecibelAvg()
    {
        Double sum = 0d;

        for(Double d : decibelList)
        {
            sum += d;
        }

        return sum/decibelList.size();
    }

    Thread runner;
    boolean runRunner;
    DecibelReader decibelReader = new DecibelReader();
    TextView decibelAvg;
    TextView decibelMin;
    TextView decibelMax;
    Speedometer speedometer;
    private static final String TAG = "DecibelsActivity";

    final Runnable updater = new Runnable() {
        public void run() {
            updateView();
        }
    };

    final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decibels);
        speedometer = (Speedometer) findViewById(R.id.Speedometer);

        try {
            decibelAvg = (TextView) findViewById(R.id.decibelAvg);
            decibelMax = (TextView) findViewById(R.id.decibelMax);
            decibelMin = (TextView) findViewById(R.id.decibelMin);

        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }

        addListenerOnButton();
    }

    private void startRunner() {
        runRunner = true;
        if (runner == null) {
            runner = new Thread() {
                public void run() {
                    while (runner != null && runRunner) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        ;
                        mHandler.post(updater);
                    }
                }
            };
            runner.start();
        }
    }

    private void stopRunner() {
        runRunner = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        decibelList.clear();
        decibelReader.start();
        startRunner();
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopRunner();
        decibelReader.stop();
    }

    void updateView() {
        double currentReading =  decibelReader.getAmplitudeEMA();
        String currentFormattedReading = String.format("%.2f", currentReading);

        decibelList.add(currentReading);
        String avg = String.format("%.2f", getDecibelAvg());
        decibelAvg.setText(avg);
        String min = String.format("%.2f", getDecibelMin());
        decibelMin.setText(min);
        String max = String.format("%.2f", getDecibelMax());
        decibelMax.setText(max);

        // Update the decibel graph.
        speedometer.onSpeedChanged((float)currentReading);

        if (BuildConfig.DEBUG) {
            Log.d(getLocalClassName(), String.format("Decibel readings: current: %s, min: %s, max: %s, avg: %s", currentFormattedReading, min, max, avg));
        }
    }

    public void addListenerOnButton() {

        Button decibelResest = (Button) findViewById(R.id.decibelResest);

        decibelResest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                decibelList.clear();
            }
        });

    }
}
