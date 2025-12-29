package com.dualnback.ui.mainscreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dualnback.data.filesystem.dao.DataPoint;
import com.dualnback.data.filesystem.dao.DataPointCollection;
import com.dualnback.data.filesystem.dao.FileBasedDao;
import com.dualnback.data.filesystem.io.FileIO;
import com.dualnback.data.filesystem.util.FileUtil;
import com.dualnback.data.location.LocationCollection;
import com.dualnback.data.settings.ApplicationConfig;
import com.dualnback.data.settings.ConfigReader;
import com.dualnback.data.sound.SoundCollection;
import com.dualnback.game.NBackVersion;
import com.dualnback.game.factory.GameParameters;
import com.dualnback.game.factory.SoundCollectionFactory;
import com.mainscreen.ui.continuescreen.ContinueActivity;
import com.monkeyladder.R;

import java.util.Date;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

import static com.dualnback.data.filesystem.dao.DataFileUtil.readAllData;
import static com.dualnback.game.VersionSelection.currentLevel;
import static com.dualnback.ui.mainscreen.MainActivityPresenter.TIME_TEXT_NORMAL_COLOUR;
import static com.dualnback.util.DateUtil.*;
import static com.monkeyladder.R.drawable.nback_checkmark;
import static com.monkeyladder.R.drawable.nback_xmark;

public class MainActivity extends AppCompatActivity implements MainScreenView {

    public static final int MIN_REQUIRED_SC0RE_TO_GO_TO_NEXT_LVL = 80;
    public static final int MIN_REQUIRED_SC0RE_TO_MAINTAIN_CURRENT_LVL = 60;

    public static final String FINAL_SCORE = "FINAL_SCORE";
    public static final String VERSION = "VERSION";
    public static final int EXPECTED_SOUND_MATCHES = 7;
    public static final int EXPECTED_LOC_MATCHES = 7;
    public static final int TOTAL_TRIAL_COUNT = 25;
    public static final int COUNT_DOWN_INTERVAL_IN_MILLIS = 1000;

    private final Timer gameUpdateTimer = new Timer( false );
    private TextView countDownTextView;
    private ImageButton soundMatchButton;
    private ImageButton locationMatchButton;
    private TextView scoreTxt;
    private ImageView positionMatchFeedBackImg;
    private ImageView soundMatchFeedBackImg;
    private TextView gameVersionText;
    private NBackVersion version;
    private Vibrator vibrator;

    private MainActivityPresenter presenter = null;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        Log.d("DualNBack", "=== onCreate START ===");
        Log.d("DualNBack", "Calling getCurrentVersion with thresholds: upgrade=" + MIN_REQUIRED_SC0RE_TO_GO_TO_NEXT_LVL + ", maintain=" + MIN_REQUIRED_SC0RE_TO_MAINTAIN_CURRENT_LVL);

        version = getCurrentVersion( MIN_REQUIRED_SC0RE_TO_GO_TO_NEXT_LVL,
                MIN_REQUIRED_SC0RE_TO_MAINTAIN_CURRENT_LVL );

        Log.d("DualNBack", "Selected version: " + version.getTextRepresentation());
        Log.d("DualNBack", "=== onCreate END ===");

        if ( presenter == null ) {
            presenter = new MainActivityPresenter( this,
                    new GameParameters()
                            .withVersion( version )
                            .withContext( this )
                            .withExpectedSoundMatches( EXPECTED_SOUND_MATCHES )
                            .withExpectedLocationMatches( EXPECTED_LOC_MATCHES )
                            .withLocationCollection( new LocationCollection() )
                            .withSoundCollection( new SoundCollection( SoundCollectionFactory.instance( this ) ) )
                            .withConfig( new ApplicationConfig( new ConfigReader( this ) ) ) );
        }


        setContentView( R.layout.nback_activity_main );

        initGameUiElements( version );

        countDownTextView = findViewById( R.id.textViewCountDownTimer );

        vibrator = ( Vibrator ) getSystemService( Context.VIBRATOR_SERVICE );

        locationMatchButton.setOnClickListener( ( View view ) -> {
            presenter.handleLocationButtonClick();
        } );

        soundMatchButton.setOnClickListener( ( View view ) -> {
            presenter.handleSoundButtonClick();
        } );

        presenter.startTrial();
    }

    @Override
    public void onStart( ) {
        super.onStart();

        presenter.startTimer();
    }

    @Override
    public void onPause( ) {
        super.onPause();

        presenter.pauseTimer();
    }

    public void updateLocationFeedBackImage( ) {
        gameUpdateTimer.schedule( new TimerTask() {
            @Override
            public void run( ) {
                runOnUiThread( ( ) ->
                        positionMatchFeedBackImg.setImageResource( R.drawable.nback_transparent ) );
            }
        }, 500 );
    }

    @Override
    public void updateSoundFeedBackImage( ) {
        gameUpdateTimer.schedule( new TimerTask() {
            @Override
            public void run( ) {
                runOnUiThread( ( ) ->
                        soundMatchFeedBackImg.setImageResource( R.drawable.nback_transparent ) );
            }
        }, 500 );
    }

    @Override
    public void setPositionMatchFeedBack( boolean isCorrectAnswer ) {
        positionMatchFeedBackImg.setImageResource( isCorrectAnswer ? nback_checkmark : nback_xmark );
    }

    @Override
    public void setSoundMatchFeedBack( boolean isCorrectAnswer ) {
        soundMatchFeedBackImg.setImageResource( isCorrectAnswer ? nback_checkmark : nback_xmark );
    }

    @Override
    public void vibrateFor( int vibrationLength ) {
        vibrator.vibrate( vibrationLength );
    }

    @Override
    public void setCountDownText( String text ) {
        setCountDownTextAndColor( text, TIME_TEXT_NORMAL_COLOUR );
    }

    @Override
    public void setCountDownTextAndColor( String text, int color ) {
        countDownTextView.setTextColor( color );
        countDownTextView.setText( text );
    }

    @Override
    public void setScoreTextRound( String text ) {
        scoreTxt.setText( text );
    }

    public void onFinish( double currentScore ) {
        Log.d("DualNBack", "=== GAME FINISHED ===");
        Log.d("DualNBack", "Final score: " + currentScore + ", version: " + version.getTextRepresentation());

        setCountDownText( "00:00" );

        Date date = new Date();

        // Save game result with version info for level adjustment
        saveGameResult( date, (int) currentScore, version );

        Intent continueIntent = new Intent( this, ContinueActivity.class );

        continueIntent.putExtra( ContinueActivity.EXTRA_TITLE, version.getTextRepresentation() );
        continueIntent.putExtra( ContinueActivity.EXTRA_ICON_RES_ID, R.drawable.dual_n_back_main_icon );
        continueIntent.putExtra( ContinueActivity.EXTRA_SCORE_TEXT, "Score " + com.dualnback.util.NumberFormatterUtil.formatScore( currentScore ) );
        continueIntent.putExtra( ContinueActivity.EXTRA_REPLAY_ACTIVITY, "com.dualnback.ui.mainscreen.MainActivity" );
        continueIntent.putExtra( ContinueActivity.EXTRA_SHOW_STATS, true );
        continueIntent.putExtra( com.chart.ui.ChartActivityIntent.FINAL_SCORE, (int) currentScore );
        continueIntent.putExtra( com.chart.ui.ChartActivityIntent.DATE, format( date ) );

        startActivity( continueIntent );
    }

    private void saveGameResult( Date date, int score, NBackVersion version ) {
        Log.d("DualNBack", "=== SAVING GAME RESULT ===");
        Log.d("DualNBack", "Saving: date=" + date + ", score=" + score + ", version=" + version.getTextRepresentation());

        DataPoint newDataPoint = new DataPoint( date, score, version );
        DataPointCollection existingData = readAllData( getFilesDir() );
        Log.d("DualNBack", "Existing data before adding: " + existingData);

        DataPointCollection updatedData = existingData.addDataPoint( newDataPoint );
        Log.d("DualNBack", "Updated data after adding: " + updatedData);

        new FileBasedDao(
            new FileIO(FileUtil.getDataFile( getFilesDir() ))
        ).write( updatedData );

        Log.d("DualNBack", "Save completed successfully");
    }

    @Override
    public void updateCellState( int cell, int state ) {
        ImageView imageView = findViewById( cell );
        imageView.setImageResource( state );
    }

    private void initGameUiElements( NBackVersion version ) {
        soundMatchButton = findViewById( R.id.soundMatchButton );

        locationMatchButton = findViewById( R.id.positionMatchButton );

        scoreTxt = findViewById( R.id.textViewScore );

        positionMatchFeedBackImg = findViewById( R.id.positionMatchFeedBackImg );

        soundMatchFeedBackImg = findViewById( R.id.soundMatchFeedBackImg );

        gameVersionText = findViewById( R.id.textViewGameName );

        gameVersionText.setText( version.getTextRepresentation() );
    }

    private NBackVersion getCurrentVersion( int minScoreToUpgrade, int minScoreToMaintain ) {
        Log.d("DualNBack", "getCurrentVersion called");

        DataPointCollection allData = readAllData( this.getFilesDir() );
        Log.d("DualNBack", "All data points: " + allData);

        Optional<DataPoint> lastDataPoint = allData.getLastDataPoint();

        if ( lastDataPoint.isPresent() ) {
            DataPoint dp = lastDataPoint.get();
            Log.d("DualNBack", "Last data point found: date=" + dp.date() + ", score=" + dp.score() + ", version=" + dp.version().getTextRepresentation());
        } else {
            Log.d("DualNBack", "No last data point found - using default");
        }

        NBackVersion selectedVersion = currentLevel( lastDataPoint, minScoreToUpgrade, minScoreToMaintain );
        Log.d("DualNBack", "currentLevel returned: " + selectedVersion.getTextRepresentation());

        return selectedVersion;
    }
}
