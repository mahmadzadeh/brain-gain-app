package com.shapematch.ui.mainscreen;

import android.os.CountDownTimer;
import android.util.Log;

import com.shapematch.game.ShapeMatchGame;
import com.shapematch.game.UserInput;

import java.util.Locale;

public class MainActivityPresenter implements MainViewContract.Presenter {

    private static final String TAG = "ShapeMatchPresenter";
    private static final long GAME_DURATION_MILLIS = 90000; // 90 seconds
    private static final long COUNTDOWN_INTERVAL_MILLIS = 1000; // 1 second

    private final MainScreenView view;
    private ShapeMatchGame game;
    private CountDownTimer timer;
    private long remainingMillis = GAME_DURATION_MILLIS;

    public MainActivityPresenter( MainScreenView view ) {
        this.view = view;
        this.game = ShapeMatchGame.initialState;
    }

    public void displayCurrentState() {
        view.displayNewGrids( game.cellGridPair() );
        view.updateScore( game.currentPoints() );
        view.updateLevel( game.currentLevel().getShapeCount() );
    }

    @Override
    public void handleMatchButtonClick() {
        Log.d( TAG, "Match button clicked" );
        processUserInput( UserInput.Match );
    }

    @Override
    public void handleMismatchButtonClick() {
        Log.d( TAG, "Mismatch button clicked" );
        processUserInput( UserInput.Mismatch );
    }

    private void processUserInput( UserInput userInput ) {
        boolean isCorrect = game.isCorrectAnswer( userInput );

        // Show feedback
        view.showFeedback( isCorrect );

        // Evaluate and update game state
        game = game.evaluateUserInput( userInput );

        // Update UI with new state
        view.updateScore( game.currentPoints() );
        view.updateLevel( game.currentLevel().getShapeCount() );
        view.displayNewGrids( game.cellGridPair() );

        Log.d( TAG, "Score: " + game.currentPoints() +
                ", Level: " + game.currentLevel().getShapeCount() +
                ", Consecutive: " + game.correctAnswers() );
    }

    @Override
    public void startTimer() {
        Log.d( TAG, "Timer started with " + remainingMillis + "ms remaining" );

        if ( timer != null ) {
            timer.cancel();
        }

        timer = new CountDownTimer( remainingMillis, COUNTDOWN_INTERVAL_MILLIS ) {
            @Override
            public void onTick( long millisUntilFinished ) {
                remainingMillis = millisUntilFinished;
                String timeText = formatTime( millisUntilFinished );
                view.setCountDownText( timeText );
            }

            @Override
            public void onFinish() {
                MainActivityPresenter.this.onFinish();
            }
        };

        timer.start();
    }

    @Override
    public void pauseTimer() {
        Log.d( TAG, "Timer paused at " + remainingMillis + "ms" );
        if ( timer != null ) {
            timer.cancel();
        }
    }

    @Override
    public void onFinish() {
        Log.d( TAG, "Game finished - Time's up! Final score: " + game.currentPoints() );
        game = game.markGameAsFinished();
        view.onFinish( game.currentPoints() );
    }

    private String formatTime( long millis ) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format( Locale.US, "%d:%02d", minutes, seconds );
    }
}
