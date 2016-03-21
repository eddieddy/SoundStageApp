package com.example.cet191.soundstageapp;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LumensandDecibelsActivity extends LumensActivityBase {

    Thread runner;
    boolean runRunner;
    DecibelReader decibelReader = new DecibelReader();
    TextView decibelAvg;
    TextView decibelMin;
    TextView decibelMax;
    WebView webView;

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

        ImageButton btnMainTop = (ImageButton)findViewById(R.id.imgBtnGoToMainFromDecActivity);
        btnMainTop.setVisibility(View.INVISIBLE);

        try {
            decibelAvg = (TextView) findViewById(R.id.decibelAvg);
            decibelMax = (TextView) findViewById(R.id.decibelMax);
            decibelMin = (TextView) findViewById(R.id.decibelMin);

            webView = (WebView)findViewById(R.id.lumensWebView);
            // Enable Javascript.
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setUseWideViewPort(true);


            webView.loadUrl("file:///android_asset/lumensHtml.html");

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

        // Enable Javascript.
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        String function = String.format("javascript:setGraph(%d)", (int)currentReading * 10);
        webView.loadUrl(function);

        if (BuildConfig.DEBUG) {
            Log.d(getLocalClassName(), String.format("Decibel readings: current: %s, min: %s, max: %s, avg: %s", currentFormattedReading, min, max, avg));
        }
    }

    public void addListenerOnButton() {

        ImageButton mainButton = (ImageButton) findViewById(R.id.imgBtnGoToMainFromLumActivity);
        Button decibelResest = (Button) findViewById(R.id.decibelResest);

        mainButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        decibelResest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                decibelList.clear();
            }
        });
    }
}
