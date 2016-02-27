package com.example.cet191.soundstageapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class DecibelsActivity extends ActivityBaseClass {
    Thread runner;
    DecibelReader decibelReader = new DecibelReader();
    TextView decibelAvg;

    final Runnable updater = new Runnable(){

        public void run(){
            updateView();
        };
    };
    final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decibels);

        // Code added to add the menu.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            decibelAvg = (TextView) findViewById(R.id.decibelAvg);
            //  decibelAvg.setText(Double.toString(decibelReader.getAmplitudeEMA()));

        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }

        if (runner == null) {
            runner = new Thread() {
                public void run() {
                    while (runner != null) {
                        try {
                            Thread.sleep(1000);
                            Log.i("Noise", "Tock");
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

    @Override
    protected void onResume() {
        super.onResume();

        decibelReader.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        decibelReader.stop();
    }

    void updateView()
    {
        String currentReading = String.format("%.2f", decibelReader.getAmplitudeEMA());
        decibelAvg.setText(currentReading);

        addListenerOnButton();
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
}
