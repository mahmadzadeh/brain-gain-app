package com.stroop.ui.mainscreen;

import static com.stroop.ui.element.StatefulGameObject.ColourState.RedColour;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chart.filesystem.dao.GameKey;
import com.chart.filesystem.dao.GameStatsRepository;
import com.chart.ui.ChartActivityIntent;
import com.mainscreen.ui.continuescreen.ContinueActivity;
import com.monkeyladder.R;
import com.stroop.GameCountDownTimer;
import com.stroop.GameObjects;
import com.stroop.StroopGame;
import com.util.DateUtil;
import com.util.RandomBoolean;
import com.util.SoundPlayer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static final String FINAL_SCORE = "FINAL_SCORE";
    public static final String TIMER_START_TIME = "00:00";
    public final int ONE_ROUND_IN_MILLIS = 90000;
    public final int COUNT_DOWN_INTERVAL_IN_MILLIS = 1000;

    private StroopGame stroopGame = new StroopGame();

    private TextView coutdownTimerTxt;
    private TextView scoreTxt;
    private TextView gameText;
    private View leftButton;
    private View rightButton;
    private TextView leftButtonText;
    private TextView rightButtonText;
    private SoundPlayer soundPlayer;

    private GameCountDownTimer timer;

    private Handler handler;

    public void onCreate( final Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.stroop_activity_game_screen );

        coutdownTimerTxt = ( TextView ) findViewById( R.id.textViewCountDownTImer );
        gameText = ( TextView ) findViewById( R.id.textViewGameText );
        scoreTxt = ( TextView ) findViewById( R.id.textViewScore );
        leftButton = findViewById( R.id.leftButton );
        rightButton = findViewById( R.id.rightButton );
        leftButtonText = findViewById( R.id.leftButtonText );
        rightButtonText = findViewById( R.id.rightButtonText );
        soundPlayer = new SoundPlayer( this );

        timer = new GameCountDownTimer( this, ONE_ROUND_IN_MILLIS, COUNT_DOWN_INTERVAL_IN_MILLIS );

        handler = new Handler() {
            public void handleMessage( Message m ) {
                stroopGame.setCurrentState( RandomBoolean.nextRandomTrue() );
                setGameState( stroopGame.getCurrentState() );
            }
        };

        leftButton.setOnClickListener(
                v -> {

                    stroopGame.setScoreBasedOnAnswer( stroopGame.getCurrentState().getLeftButton() );
                    boolean isCorrectAnswer = stroopGame.isCorrectAnswerBasedOnInternalState( stroopGame.getCurrentState().getLeftButton() );
                    soundPlayer.soundFeedbackForUserInput( isCorrectAnswer );
                    updateUI();
                }
        );

        rightButton.setOnClickListener(
                v -> {
                    stroopGame.setScoreBasedOnAnswer( stroopGame.getCurrentState().getRightButton() );
                    boolean isCorrectAnswer = stroopGame.isCorrectAnswerBasedOnInternalState( stroopGame.getCurrentState().getRightButton() );
                    soundPlayer.soundFeedbackForUserInput( isCorrectAnswer );
                    updateUI();
                }
        );

        timer.start();

        updateUI();
    }

    public void nextActivity( ) {

        this.coutdownTimerTxt.setText( TIMER_START_TIME );

        Date date = new Date();

        Intent continueIntent = new Intent( this, ContinueActivity.class );
        continueIntent.putExtra( ContinueActivity.EXTRA_TITLE, "Stroop" );
        continueIntent.putExtra( ContinueActivity.EXTRA_ICON_RES_ID, R.drawable.stroop_icon );
        continueIntent.putExtra( ContinueActivity.EXTRA_SCORE_TEXT, "Score " + stroopGame.getScore() );
        continueIntent.putExtra( ContinueActivity.EXTRA_REPLAY_ACTIVITY, "com.stroop.ui.mainscreen.MainActivity" );
        continueIntent.putExtra( ContinueActivity.EXTRA_SHOW_STATS, true );
        continueIntent.putExtra( ContinueActivity.EXTRA_GAME_KEY, GameKey.STROOP.name() );
        continueIntent.putExtra( ChartActivityIntent.FINAL_SCORE, stroopGame.getScore() );
        continueIntent.putExtra( ChartActivityIntent.DATE, DateUtil.format( date ) );

        GameStatsRepository.create( getFilesDir() ).addScore( GameKey.STROOP, date, stroopGame.getScore() );

        startActivity( continueIntent );
    }

    @Override
    public void onPause( ) {
        super.onPause();
        timer.cancel();
    }

    @Override
    public void onStop( ) {
        super.onStop();
        timer.cancel();
    }

    public void setCountDownText( String text ) {
        coutdownTimerTxt.setText( text );
    }

    public void setGameState( GameObjects gameState ) {
        this.gameText.setText( gameState.getMainText().getTextState().toString() );
        this.gameText.setTextColor( gameState.getMainText().getColourState() == RedColour
                ? getResources().getColor( R.color.textRed )
                : getResources().getColor( R.color.textBlue ) );

        this.leftButtonText.setText( gameState.getLeftButton().getTextState().toString() );
        this.leftButtonText.setTextColor( gameState.getLeftButton().getColourState() == RedColour
                ? getResources().getColor( R.color.textRed )
                : getResources().getColor( R.color.textBlue ) );

        this.rightButtonText.setText( gameState.getRightButton().getTextState().toString() );
        this.rightButtonText.setTextColor( gameState.getRightButton().getColourState() == RedColour
                ? getResources().getColor( R.color.textRed )
                : getResources().getColor( R.color.textBlue ) );

        this.scoreTxt.setText( stroopGame.getScore().toString() );
    }

    private void updateUI( ) {
        long TIMER_DELAY = 100;
        final Timer timer = new Timer();
        timer.schedule( new TimerTask() {
            public void run( ) {
                handler.obtainMessage( 1 ).sendToTarget();
            }
        }, TIMER_DELAY );
    }
}
