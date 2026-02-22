package com.tokensearch.ui.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chart.filesystem.dao.GameKey;
import com.chart.filesystem.dao.GameStatsRepository;
import com.chart.ui.ChartActivityIntent;
import com.mainscreen.ui.continuescreen.ContinueActivity;
import com.monkeyladder.R;
import com.tokensearch.game.Box;
import com.util.DateUtil;
import com.util.SoundPlayer;

import java.util.Date;
import java.util.List;

public class TokenSearchMainActivity extends AppCompatActivity implements TokenSearchViewContract {

    private TokenSearchPresenter presenter;
    private TokenSearchGridView gameGridView;
    private TextView statusText;
    private SoundPlayer soundPlayer;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_token_search_main );

        gameGridView = findViewById( R.id.gameGridView );
        statusText = findViewById( R.id.statusText );
        soundPlayer = new SoundPlayer( this );

        gameGridView.setBoxClickListener( index -> {
            if ( presenter != null ) {
                presenter.onBoxTapped( index );
            }
        } );

        gameGridView.post( () -> {
            presenter = TokenSearchInjector.inject( this );
            presenter.startGame();
        } );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( presenter != null ) {
            presenter.cleanup();
        }
        if ( soundPlayer != null ) {
            soundPlayer.release();
        }
    }

    @Override
    public void showBoxes( List<Box> boxes, int gridCols, int gridRows ) {
        runOnUiThread( () -> gameGridView.showBoxes( boxes, gridCols, gridRows ) );
    }

    @Override
    public void revealToken( int boxIndex ) {
        runOnUiThread( () -> {
            gameGridView.revealToken( boxIndex );
            if ( soundPlayer != null ) {
                soundPlayer.playDing();
            }
        } );
    }

    @Override
    public void notifyEmptySearch() {
        runOnUiThread( this::vibrate );
    }

    @Override
    public void showError( int boxIndex ) {
        runOnUiThread( () -> {
            gameGridView.showError( boxIndex );
            vibrate();
            if ( soundPlayer != null ) {
                soundPlayer.playOver();
            }
        } );
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService( VIBRATOR_SERVICE );
        if ( vibrator != null ) {
            vibrator.vibrate( VibrationEffect.createOneShot( 30, VibrationEffect.DEFAULT_AMPLITUDE ) );
        }
    }

    @Override
    public void hideToken( int boxIndex ) {
        runOnUiThread( () -> gameGridView.hideToken( boxIndex ) );
    }

    @Override
    public void updateScore( int score ) {
        runOnUiThread( () -> {
            TextView scoreText = findViewById( R.id.scoreText );
            scoreText.setText( String.valueOf( score ) );
        } );
    }

    @Override
    public void updateLives( int lives ) {
        runOnUiThread( () -> {
            TextView livesText = findViewById( R.id.livesText );
            livesText.setText( String.valueOf( lives ) );
        } );
    }

    @Override
    public void updateLevel( int level ) {
        runOnUiThread( () -> {
            TextView levelText = findViewById( R.id.levelText );
            levelText.setText( String.valueOf( level ) );
        } );
    }

    @Override
    public void setStatusText( int resId ) {
        runOnUiThread( () -> statusText.setText( resId ) );
    }

    @Override
    public void setStatusText( int resId, Object... formatArgs ) {
        runOnUiThread( () -> statusText.setText( getString( resId, formatArgs ) ) );
    }

    @Override
    public void onLevelComplete() {
        // Handled by presenter via setStatusText
    }

    @Override
    public void onGameOver( int finalScore ) {
        runOnUiThread( () -> {
            Date date = new Date();
            saveGameResult( date, finalScore );

            Intent continueIntent = new Intent( this, ContinueActivity.class );
            continueIntent.putExtra( ContinueActivity.EXTRA_TITLE, "Token Search" );
            continueIntent.putExtra( ContinueActivity.EXTRA_ICON_RES_ID, R.drawable.tokensearch_icon );
            continueIntent.putExtra( ContinueActivity.EXTRA_SCORE_TEXT, "Score " + finalScore );
            continueIntent.putExtra( ContinueActivity.EXTRA_REPLAY_ACTIVITY,
                "com.tokensearch.ui.mainscreen.TokenSearchMainActivity" );
            continueIntent.putExtra( ContinueActivity.EXTRA_SHOW_STATS, true );
            continueIntent.putExtra( ContinueActivity.EXTRA_GAME_KEY, GameKey.TOKEN_SEARCH.name() );
            continueIntent.putExtra( ChartActivityIntent.FINAL_SCORE, finalScore );
            continueIntent.putExtra( ChartActivityIntent.DATE, DateUtil.format( date ) );

            startActivity( continueIntent );
            finish();
        } );
    }

    private void saveGameResult( Date date, int score ) {
        GameStatsRepository.create( getFilesDir() ).addScore( GameKey.TOKEN_SEARCH, date, score );
    }
}
