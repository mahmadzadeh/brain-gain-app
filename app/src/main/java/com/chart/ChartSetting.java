package com.chart;

import android.graphics.Color;

import java.util.Arrays;
import java.util.Collection;

class ChartSetting {

    public static final int GREEN = Color.rgb( 123, 220, 76 );
    public static final int RED = Color.rgb( 168, 50, 50 );
    public static final int MAX_DATA_POINT_SIZE = 40;

    public Collection<SettingPair> getSettings( ) {
        // char settings

        return Arrays.asList(
                new SettingPair( "LINE_COLOR", GREEN ),
                new SettingPair( "LINE_WIDTH", 1F ),
                new SettingPair( "VALUE_TEXT_SIZE", 6F ),
                new SettingPair( "FILL_COLOR", RED ),
                new SettingPair( "MAX_DATA_POINTS", MAX_DATA_POINT_SIZE )

        );
    }
}
