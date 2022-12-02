package com.monkeyladder.util;

public class ProgressCounter {

    private final int duration;
    private final int oneTick;
    private final int counts;
    private final int increments;

    private int progress;

    public ProgressCounter( int duration, int oneTick ) {

        this.duration = duration;
        this.oneTick = oneTick;
        counts = duration / oneTick;
        increments = 100 / counts;
        progress = 0;
    }


    public int getNextProgressPercentage( ) {
        progress += increments;

        return progress;
    }

    public void reset( ) {
        progress = 0;
    }
}
