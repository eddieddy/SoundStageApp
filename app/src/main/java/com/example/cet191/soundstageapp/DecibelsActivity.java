package com.example.cet191.soundstageapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class DecibelsActivity extends ActivityBaseClass {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decibels);

        // Code added to add the menu.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
