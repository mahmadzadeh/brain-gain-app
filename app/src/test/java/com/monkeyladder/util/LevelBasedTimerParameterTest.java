package com.monkeyladder.util;

import com.monkeyladder.game.GameLevel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LevelBasedTimerParameterTest {

    private static final int DELAY_PER_CELL_COUNT = 800;
    private static final int ONE_TICK_IN_MILLIS = 1000;
    private LevelBasedTimerParameter levelBasedTimerParameter;

    @Before
    public void setUp( ) {
        levelBasedTimerParameter = new LevelBasedTimerParameter();
    }

    @After
    public void tearDown( ) {
    }

    @Test
    public void givenGameLevelTwo_thenTimerParameterIsCorrect( ) {

        GameLevel level = GameLevel.LevelTwo;
        CountDownTimerParameter expected = new CountDownTimerParameter( 1600, ONE_TICK_IN_MILLIS );

        CountDownTimerParameter actual = levelBasedTimerParameter.getTimerParametersForLevel( level, DELAY_PER_CELL_COUNT, ONE_TICK_IN_MILLIS );

        assertEquals( expected, actual );
    }

    @Test
    public void givenGameLevelThree_thenTimerParameterIsCorrect( ) {

        GameLevel level = GameLevel.LevelThree;

        CountDownTimerParameter expected = new CountDownTimerParameter( 2400, ONE_TICK_IN_MILLIS );

        CountDownTimerParameter actual = levelBasedTimerParameter.getTimerParametersForLevel( level, DELAY_PER_CELL_COUNT, ONE_TICK_IN_MILLIS );

        assertEquals( expected, actual );
    }

}