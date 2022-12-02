package com.monkeyladder.util;

import com.monkeyladder.game.GameLevel;

public class LevelBasedTimerParameter {

    /**
     * Return timer parameters based on the number cells user has to keep track of
     *
     * @param level
     * @param delayPerCellCount
     * @param oneTickInMillis
     * @return
     */
    public CountDownTimerParameter getTimerParametersForLevel( GameLevel level, int delayPerCellCount, int oneTickInMillis ) {
        return new CountDownTimerParameter( level.cellCount() * delayPerCellCount, oneTickInMillis );
    }
}

