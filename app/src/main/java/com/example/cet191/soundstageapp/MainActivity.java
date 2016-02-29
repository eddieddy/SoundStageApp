package com.example.cet191.soundstageapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends ActivityBaseClass {
//hello
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
