package com.example.cet191.soundstageapp;

import android.media.MediaRecorder;

import java.io.IOException;

/**
 * Created by Eddie on 2/24/2016.
 */
public class DecibelReader {
    // This file is used to record voice
    static final private double EMA_FILTER = 0.4;

    private MediaRecorder mRecorder = null;
    private double mEMA = 0.0;
    private double ampl = Math.pow(10, -3);

    public boolean isRunning (){
        return mRecorder != null;
    }

    public DecibelReader() {

    }

    public void start() {

        if (mRecorder == null) {
            try {
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile("/dev/null");

                try {
                    mRecorder.prepare();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                mRecorder.start();
                mEMA = 0.0;
            } catch (Exception eex) {
                // Eat it!
                mRecorder = null;
            }
        }
    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double soundDb(){
        double db =   20 * Math.log10(getAmplitudeEMA() / ampl);
        if(db <0)
        {
            return 0;
        }
        else
        {
            return db;
        }
    }
    public double getAmplitude() {
        if (mRecorder != null) {
            return (mRecorder.getMaxAmplitude() / 2700.0);
        } else {
            return 0;
        }
    }

    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;

        return mEMA;
    }
}
