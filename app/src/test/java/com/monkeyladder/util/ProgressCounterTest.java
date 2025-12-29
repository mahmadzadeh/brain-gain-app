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

        // counts = 3000/400 = 7, increments = 100.0/7 = 14.285714...
        // First:  0 + 14.285714 = 14.285714 -> rounds to 14
        // Second: 14.285714 + 14.285714 = 28.571428 -> rounds to 29
        // Third:  28.571428 + 14.285714 = 42.857142 -> rounds to 43
        assertEquals( 14, counter.getNextProgressPercentage() );
        assertEquals( 29, counter.getNextProgressPercentage() );
        assertEquals( 43, counter.getNextProgressPercentage() );
    }

}