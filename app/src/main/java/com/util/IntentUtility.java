package com.util;

import static com.monkeyladder.ui.continuescreen.ContinueScreenActivity.FINAL_SCORE;

import android.os.Bundle;

import com.chart.filesystem.dao.DataPoint;
import com.chart.ui.ChartActivityIntent;

import java.util.Date;
import java.util.Optional;


public class IntentUtility {

    private static final float DEFAULT_SCORE_WHEN_MISSING_IN_BUNDLE = 0.00f;

    public static <T> T extractFromExtrasWithDefault( Bundle extras, String key, T defaultValue ) {
        T value = defaultValue;

        if ( extras != null ) {
            value = ( T ) extras.get( key );
        }

        return value != null ? value : defaultValue;
    }

    public static <T> Optional<T> extractFromIntentExtras( Bundle extras, String key ) {
        T value = null;

        if ( extras != null ) {
            value = ( T ) extras.get( key );
        }

        return Optional.ofNullable( value );
    }

    public static DataPoint extractDatePointFromExtras( Bundle extras ) {

        Date date = new Date();

        int score = 0;

        if ( extras != null ) {
            score = extras.getInt( FINAL_SCORE );

            date = DateUtil.parse( extras.getString( ChartActivityIntent.DATE ) );

        }

        return new DataPoint( date, score );
    }
}
