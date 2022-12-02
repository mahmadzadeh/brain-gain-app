package com.util;

import java.util.Timer;
import java.util.TimerTask;

public class TimerUtil {

    private final long timerInterval;
    private final long timerDelay;

    protected Timer timer = new Timer();
    protected TimerTask task;

    public TimerUtil( TimerTask task, long timerInterval1, long timerDelay1 ) {
        this.timerInterval = timerInterval1;
        this.timerDelay = timerDelay1;
        this.task = task;
    }

    public void start( ) {
        timer.scheduleAtFixedRate( task, timerInterval, timerDelay );
    }

    public void pause( ) {
        timer.cancel();
        timer.purge();
    }

    public void resume( ) {
        timer = new Timer();
        timer.scheduleAtFixedRate( task, timerInterval, timerDelay );
    }
}
