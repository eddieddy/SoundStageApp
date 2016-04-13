package com.example.cet191.soundstageapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Eddie on 2/9/2016.
 */
public class ActivityBaseClass extends AppCompatActivity {
    DecibelReader decibelReader = new DecibelReader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Code added to add the menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    // Code added to add the menu.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.display_decibels) {
            Intent i = new Intent(this, DecibelsActivity.class);
            startActivity(i);
        } else if (id == R.id.display_lumens) {
            Intent i = new Intent(this, LumensActivity.class);
            startActivity(i);
        } else if (id == R.id.display_lumensanddecibels) {
            Intent i = new Intent(this, LumensandDecibelsActivity.class);
            startActivity(i);
        } else if (id == R.id.display_main) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
}
