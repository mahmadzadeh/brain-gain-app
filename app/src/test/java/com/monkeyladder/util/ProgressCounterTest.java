package com.monkeyladder.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProgressCounterTest {

    @Test
    public void givenDivisibleDurationAndTick_thenProgressPercentageCanBeObtained( ) {
        int duration = 2000;
        int oneTick = 100;

        ProgressCounter counter = new ProgressCounter( duration, oneTick );

        assertEquals( 5, counter.getNextProgressPercentage() );
        assertEquals( 10, counter.getNextProgressPercentage() );
        assertEquals( 15, counter.getNextProgressPercentage() );
    }

    @Test
    public void givenIndivisibleDurationAndTick_thenProgressPercentageCanBeObtained( ) {
        int duration = 3000;
        int oneTick = 400;

        ProgressCounter counter = new ProgressCounter( duration, oneTick );

        assertEquals( 14, counter.getNextProgressPercentage() );
        assertEquals( 28, counter.getNextProgressPercentage() );
        assertEquals( 42, counter.getNextProgressPercentage() );
    }

}