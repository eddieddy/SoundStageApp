package com.example.cet191.soundstageapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends ActivityBaseClass {
//hello
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();
    }

    public void addListenerOnButton() {

        ImageButton decibelButton = (ImageButton) findViewById(R.id.imageButton2);
        ImageButton luminsButton = (ImageButton) findViewById(R.id.imageButton);
        ImageButton decLumButton = (ImageButton)findViewById(R.id.imgBtnLumAndDec);

        luminsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent (v.getContext(), LumensActivity.class);
                startActivityForResult(intent, 0);
            }

        });

       decibelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), DecibelsActivity.class);
                startActivityForResult(intent, 0);
            }

        });

        decLumButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), LumensandDecibelsActivity.class);
                startActivityForResult(intent, 0);
            }

        });
    }

    public void onDisplayLumensClick(View v)
    {
        Intent i = new Intent(this, LumensActivity.class);
        startActivity(i);
    }
}
