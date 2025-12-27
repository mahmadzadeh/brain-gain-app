package com.monkeyladder.util;

import android.os.CountDownTimer;

import com.monkeyladder.ui.mainscreen.MainActivityPresenterContract;

public class GameCountDownTimer implements DisplayCountDownTimerContract {

    private final MainActivityPresenterContract presenter;
    private ProgressCounter progressCounter;
    private CountDownTimer timer;

    private GameCountDownTimer( MainActivityPresenterContract presenter ) {
        this.presenter = presenter;
    }

    public static GameCountDownTimer INSTANCE( MainActivityPresenterContract presenter ) {
        return new GameCountDownTimer( presenter );
    }

    @Override
    public void start( int timeInterval, int oneTickDuration ) {
        this.progressCounter = new ProgressCounter( timeInterval, oneTickDuration );
        this.timer = createTimer( this, timeInterval, oneTickDuration );
        this.timer.start();
    }

    @Override
    public void onTick( long millisUntilFinished ) {
        presenter.setDisplayGameBoardProgress( progressCounter.getNextProgressPercentage() );

        int secondsLeft = ( int ) Math.ceil( millisUntilFinished / 1000.0 );
        presenter.setDisplayGameBoardCountdownText( String.valueOf( Math.max( 0, secondsLeft ) ) );
    }

    @Override
    public void onFinish( ) {
        // Ensure we always end at 100% so the ring is fully closed.
        presenter.setDisplayGameBoardProgress( 100 );
        presenter.setDisplayGameBoardCountdownText( "" );

        progressCounter.reset();
        presenter.onDisplayTimerFinish();
    }

    @Override
    public void onPause( ) {
        if ( timer != null ) {
            timer.cancel();
        }
    }

    private CountDownTimer createTimer( final GameCountDownTimer gameCountDownTimer,
                                        long timeInterval, long oneTickDuration ) {

        CountDownTimer countDownTimer = new CountDownTimer( timeInterval, oneTickDuration ) {
            @Override
            public void onTick( long millisUntilFinished ) {
                gameCountDownTimer.onTick( millisUntilFinished );
            }

            @Override
            public void onFinish( ) {
                gameCountDownTimer.onFinish();
            }
        };

        return countDownTimer;
    }

}
