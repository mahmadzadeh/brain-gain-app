package com.simonsays.ui.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.chart.filesystem.dao.DataPoint;
import com.chart.filesystem.dao.DataPointCollection;
import com.chart.filesystem.dao.Dao;
import com.chart.filesystem.dao.FileBasedDao;
import com.chart.filesystem.io.FileIO;
import com.chart.filesystem.util.FileUtil;
import com.chart.ui.ChartActivityIntent;
import com.mainscreen.ui.continuescreen.ContinueActivity;
import com.monkeyladder.R;
import com.simonsays.game.Sequence;
import com.simonsays.game.SimonSaysGame;
import com.util.DateUtil;

import java.io.File;
import java.util.Date;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class SimonSaysMainActivity extends AppCompatActivity implements SimonSaysViewContract {

    private static final int TILE_COUNT = SimonSaysGame.GRID_TOTAL_TILES;
    private static final String SCORES_FILE = "simonsays_scores.json";

    private SimonSaysPresenter presenter;
    private final View[] tileViews = new View[TILE_COUNT];
    private TextView statusText;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_simon_says_main );

        statusText = findViewById( R.id.statusText );
        initTileViews();

        SimonSaysGame game = new SimonSaysGame(
            new Sequence( TILE_COUNT, new Random() ),
            SimonSaysGame.INITIAL_LIVES
        );

        presenter = new SimonSaysPresenter(
            this, game, new Handler( Looper.getMainLooper() )
        );

        presenter.startGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.cleanup();
    }

    private void initTileViews() {
        for ( int i = 0; i < TILE_COUNT; i++ ) {
            int resId = getResources().getIdentifier( "tile_" + i, "id", getPackageName() );
            tileViews[i] = findViewById( resId );

            final int tileIndex = i;
            tileViews[i].setOnClickListener( v -> presenter.onTileTapped( tileIndex ) );
        }
    }

    // SimonSaysViewContract implementations

    @Override
    public void flashTile( int tileIndex ) {
        runOnUiThread( () ->
            tileViews[tileIndex].setBackgroundResource( R.drawable.simonsays_tile_bg_flash )
        );
    }

    @Override
    public void resetTile( int tileIndex ) {
        runOnUiThread( () ->
            tileViews[tileIndex].setBackgroundResource( R.drawable.simonsays_tile_bg )
        );
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
    public void updateRound( int round ) {
        runOnUiThread( () -> {
            TextView roundText = findViewById( R.id.roundText );
            roundText.setText( String.valueOf( round ) );
        } );
    }

    @Override
    public void setStatusText( String text ) {
        runOnUiThread( () -> statusText.setText( text ) );
    }

    @Override
    public void setTilesEnabled( boolean enabled ) {
        runOnUiThread( () -> {
            for ( View tile : tileViews ) {
                tile.setClickable( enabled );
            }
        } );
    }

    @Override
    public void onGameOver( int finalScore ) {
        runOnUiThread( () -> {
            Date date = new Date();
            saveGameResult( date, finalScore );

            Intent continueIntent = new Intent( this, ContinueActivity.class );
            continueIntent.putExtra( ContinueActivity.EXTRA_TITLE, "Simon Says" );
            continueIntent.putExtra( ContinueActivity.EXTRA_ICON_RES_ID, R.drawable.simonsays_icon );
            continueIntent.putExtra( ContinueActivity.EXTRA_SCORE_TEXT,
                "Score " + finalScore );
            continueIntent.putExtra( ContinueActivity.EXTRA_REPLAY_ACTIVITY,
                "com.simonsays.ui.mainscreen.SimonSaysMainActivity" );
            continueIntent.putExtra( ContinueActivity.EXTRA_SHOW_STATS, true );
            continueIntent.putExtra( ChartActivityIntent.FINAL_SCORE, finalScore );
            continueIntent.putExtra( ChartActivityIntent.DATE, DateUtil.format( date ) );

            startActivity( continueIntent );
            finish();
        } );
    }

    private void saveGameResult( Date date, int score ) {
        DataPoint newDataPoint = new DataPoint( date, score );
        DataPointCollection existingData = readAllData( getFilesDir() );
        existingData.addDataPoint( newDataPoint );
        DataPointCollection shrunkData = existingData.shrinkDataSize();

        new FileBasedDao(
            new FileIO( FileUtil.getDataFile( getFilesDir(), SCORES_FILE ) )
        ).write( shrunkData );
    }

    private DataPointCollection readAllData( File filesDir ) {
        Dao dao = new FileBasedDao(
            new FileIO( FileUtil.getDataFile( filesDir, SCORES_FILE ) )
        );
        return dao.read();
    }
}
