package com.monkeyladder.util;

import android.os.Bundle;

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
}
