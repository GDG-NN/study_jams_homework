package com.andrejjj.pocketsecretary.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.andrejjj.pocketsecretary.R;

/**
 * @author Andrey S. Pugachenko
 * @version 0.0.1
 *          This is an Activity for day events
 */
public class DayEvents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_events);
    }
}
