package com.monkeyladder.util;

public interface DisplayCountDownTimerContract {

    void onTick( long millisUntilFinished );

    void onFinish( );

    void start( int timeInterval, int oneTickDuration );

    void onPause( );
}
