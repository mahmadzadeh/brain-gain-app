package com.monkeyladder.util;

public class ProgressCounter {

    private final int counts;
    private final double increments;

    private double progress;

    public ProgressCounter( int duration, int oneTick ) {

        counts = duration / oneTick;
        increments = 100.0 / counts;
        progress = 0;
    }

    public int getNextProgressPercentage( ) {
        progress += increments;

        return ( int ) Math.min( 100, Math.round( progress ) );
    }

    public void reset( ) {
        progress = 0;
    }
}
