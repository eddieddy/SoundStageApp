package com.example.cet191.soundstageapp;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LumensandDecibelsActivity extends LumensActivityBase {

    Thread runner;
    boolean runRunner;
    TextView decibelAvg;
    TextView decibelMin;
    TextView decibelMax;
    Speedometer speedometer;
    Button btnResetLumens;
    private static final String TAG = "LumensandDecibelsActivity";

    private List<Double> decibelList = new ArrayList<Double>();

    private Double getDecibelMin() {
        return Collections.min(decibelList);
    }

    private Double getDecibelMax() {
        return Collections.max(decibelList);
    }

    private Double getDecibelAvg() {
        Double sum = 0d;

        for (Double d : decibelList) {
            sum += d;
        }

        return sum / decibelList.size();
    }

    final Runnable updater = new Runnable() {
        public void run() {
            updateView();
        }
    };

    final Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lumensand_decibels);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (min == null)
            min = (TextView) findViewById(R.id.txtLumensMin);
        if (max == null)
            max = (TextView) findViewById(R.id.txtLumensMax);
        if (avg == null)
            avg = (TextView) findViewById(R.id.txtLumensAvg);

        if (btnResetLumens == null) {
            btnResetLumens = (Button) findViewById(R.id.btnResetLumens);

            btnResetLumens.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    resetLightReadings();
                    decibelList.clear();
                }
            });
        }

        try {
            if (decibelAvg == null)
                decibelAvg = (TextView) findViewById(R.id.decibelAvg);
            if (decibelMax == null)
                decibelMax = (TextView) findViewById(R.id.decibelMax);
            if (decibelMin == null)
                decibelMin = (TextView) findViewById(R.id.decibelMin);

            // Instantiate our graph for decibels.
            if (speedometer == null) {
                speedometer = (Speedometer) findViewById(R.id.Speedometer);
            }

            if (lumensmeter == null) {
                lumensmeter = (Speedometer) findViewById(R.id.Lumensmeter);
            }

        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
/*
        Button decibelResest = (Button) findViewById(R.id.decibelResest);
        decibelResest.setVisibility(View.INVISIBLE);*/

/*        CustomScrollView myScrollView = (CustomScrollView) findViewById(R.id.myScroll);
        //myScrollView.setEnableScrolling(false); // disable scrolling
        myScrollView.setEnableScrolling(true); // enable scrolling*/
    }

    private void startRunner() {
        runRunner = true;

        runner = new Thread() {
            public void run() {
                while (runner != null && runRunner) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                    }

                    mHandler.post(updater);
                }
            }
        };

        runner.start();
    }

    private void stopRunner() {
        runRunner = false;
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
        }
        try {
            runner.join();
            runner = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        decibelList.clear();
        decibelReader.start();
        if (decibelReader.isRunning()) {
            startRunner();
        } else {
            Toast.makeText(this,
                    "Either your need to allow microphone permission or your device does not allow access to microphone!",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopRunner();
        decibelReader.stop();
    }

    void updateView() {
        //double currentReading = decibelReader.getAmplitudeEMA();


        double currentReading = decibelReader.soundDb();
        String currentFormattedReading = String.format("%.2f", currentReading);

        decibelList.add(currentReading);
        String avg = String.format("Avg: %.2f", getDecibelAvg());
        decibelAvg.setText(avg);
        String min = String.format("Min: %.2f", getDecibelMin());
        decibelMin.setText(min);
        String max = String.format("Max: %.2f", getDecibelMax());
        decibelMax.setText(max);

        // Update the decibel graph.
        speedometer.setCurrentSpeed((float) currentReading);

        if (BuildConfig.DEBUG) {
            Log.d(getLocalClassName(), String.format("Decibel readings: current: %s, min: %s, max: %s, avg: %s", currentFormattedReading, min, max, avg));
        }
    }

    // Code added to add the menu.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        decibelReader.stop();

        return super.onOptionsItemSelected(item);
    }
}
