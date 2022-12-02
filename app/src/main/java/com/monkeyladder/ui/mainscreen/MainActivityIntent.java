package com.monkeyladder.ui.mainscreen;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivityIntent {
    private final Intent mainIntent;
    private final AppCompatActivity activity;

    public MainActivityIntent( AppCompatActivity activity ) {
        mainIntent = new Intent( activity, MainActivity.class );
        this.activity = activity;
    }

    public void startActivity( ) {
        this.activity.startActivity( mainIntent );
    }
}
