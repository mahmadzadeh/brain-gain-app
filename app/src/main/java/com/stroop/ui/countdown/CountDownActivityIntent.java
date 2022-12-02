package com.stroop.ui.countdown;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class CountDownActivityIntent {
    private final Intent mainIntent;
    private final AppCompatActivity activity;

    public CountDownActivityIntent( AppCompatActivity activity ) {
        mainIntent = new Intent( activity, CountDownActivity.class );
        this.activity = activity;
    }

    public void startActivity( ) {
        this.activity.startActivity( mainIntent );
    }
}
