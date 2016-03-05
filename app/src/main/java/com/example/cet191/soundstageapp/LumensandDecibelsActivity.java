package com.example.cet191.soundstageapp;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class LumensandDecibelsActivity extends LumensActivityBase {
    Thread runner;
    boolean runRunner;
    DecibelReader decibelReader = new DecibelReader();
    TextView decibelAvg;

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

        ImageButton btnMain = (ImageButton)findViewById(R.id.imgBtnGoToMainFromDecActivity);
        btnMain.setVisibility(View.INVISIBLE);

        try {
            decibelAvg = (TextView) findViewById(R.id.decibelAvg);
            //  decibelAvg.setText(Double.toString(decibelReader.getAmplitudeEMA()));

        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
    }

    private void startRunner() {
        runRunner = true;
        if (runner == null) {
            runner = new Thread() {
                public void run() {
                    while (runner != null && runRunner) {
                        try {
                            Thread.sleep(250);
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

        decibelReader.start();
        startRunner();
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopRunner();
        decibelReader.stop();
    }

    void updateView()
    {
        String currentReading = String.format("%.2f", decibelReader.getAmplitudeEMA());
        decibelAvg.setText(currentReading);
        if (BuildConfig.DEBUG) {
            Log.d(this.getLocalClassName(), "Decibels read: " + currentReading);
        }
    }
}
