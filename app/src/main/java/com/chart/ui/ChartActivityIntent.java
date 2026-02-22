package com.chart.ui;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.util.DateUtil;

import java.util.Date;

public class ChartActivityIntent {

    public static final String DATE = "GAME_DATE";
    public static final String FINAL_SCORE = "FINAL_SCORE";

    private final Intent chartIntent;
    private final AppCompatActivity activity;

    public ChartActivityIntent( AppCompatActivity activity ) {
        chartIntent = new Intent( activity, ChartActivity.class );
        this.activity = activity;
    }

    public ChartActivityIntent addScore( int score ) {
        chartIntent.putExtra( FINAL_SCORE, score );
        return this;
    }

    public ChartActivityIntent addDate( Date date ) {
        chartIntent.putExtra( DATE, DateUtil.format( date ) );
        return this;
    }

    public void startActivity( ) {
        this.activity.startActivity( chartIntent );
    }
}
