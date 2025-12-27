package com.monkeyladder.ui.mainscreen;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chart.ui.ChartActivityIntent;
import com.monkeyladder.R;
import com.monkeyladder.game.GameLevel;
import com.monkeyladder.game.GameState;
import com.monkeyladder.game.Location;
import com.monkeyladder.game.MonkeyLadderGame;
import com.monkeyladder.game.PlayerLives;
import com.monkeyladder.util.CellDataMapping;
import com.monkeyladder.util.CellLocationMapping;
import com.monkeyladder.util.LevelBasedTimerParameter;
import com.monkeyladder.util.LocationData;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MainActivityViewContract, View.OnClickListener {

    private static final int DELAY_PER_CELL_COUNT_MILLIS = 1000;
    private static final int ONE_TICK_IN_MILLIS = 750;
    private static final int DELAY_ON_INITIAL_SCREEN_DISPLAY_MILLIS = 1000;

    private static final CellLocationMapping locationMapping = new CellLocationMapping();
    private static final CellDataMapping dataMapping = new CellDataMapping();
    private static final LevelBasedTimerParameter timerParam = new LevelBasedTimerParameter();


    private static final GameLevel STARTING_LEVEL = GameLevel.LevelTwo;
    private final Timer gameUpdateTimer = new Timer( false );
    private MainActivityPresenter presenter = null;
    private ProgressBar progressBar;
    private TextView countdownText;
    private ImageView resultImage;
    private ImageView life1;
    private ImageView life2;
    private ImageView life3;

    private boolean isReadyForUserInput = false;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        presenter = new MainActivityPresenter( this,
                new MainScreenModel( new MonkeyLadderGame( STARTING_LEVEL ) ) );

        setContentView( R.layout.monkey_ladder_activity_main );

        initUserInterfaceElements();

        delayDisplayingRound( DELAY_ON_INITIAL_SCREEN_DISPLAY_MILLIS );

        presenter.startOneRound();
    }

    @Override
    public void displayBoard( List<LocationData> locationsThatAreSet ) {

        // Hide all cells, then show only the numbered ones.
        clearScreen();

        locationsThatAreSet
                .stream()
                .forEach( locationData -> {
                    Integer resourceId = locationMapping
                            .resourceIdForLocation( locationData.getLocation() )
                            .orElseThrow( ( ) -> new RuntimeException( "" ) );

                    ImageView imageView = findViewById( resourceId );
                    imageView.setVisibility( View.VISIBLE );
                    imageView.setBackgroundResource( R.drawable.monkey_ladder_cell_display );
                    imageView.setImageResource(
                            dataMapping.drawableResourceIdFor( locationData.getData() )
                                    .orElseThrow( ( ) -> new RuntimeException(
                                            "Unable to get the resource for data" + locationData.getData() ) ) );

                } );
    }

    @Override
    public void updateDisplayBoardProgressBar( int progress ) {
        progressBar.setProgress( progress );
    }

    @Override
    public void updateDisplayBoardCountdownText( String text ) {
        if ( countdownText != null ) {
            countdownText.setText( text );
        }
    }

    @Override
    public void clearScreen( ) {
        locationMapping
                .getMapping()
                .keySet()
                .stream()
                .forEach( resourceId -> clearResource( resourceId ) );

    }

    @Override
    public void showInputBoard( List<LocationData> locationsThatAreSet ) {

        // Hide all cells, then show only the ones that were numbered.
        clearScreen();

        int startColor = getResources().getColor( R.color.monkeyLadderCellLight );
        int endColor = getResources().getColor( R.color.colorPrimaryDark );

        locationsThatAreSet
                .stream()
                .forEach( locationData -> {
                    Integer resourceId = locationMapping
                            .resourceIdForLocation( locationData.getLocation() )
                            .orElseThrow( ( ) -> new RuntimeException( "" ) );

                    ImageView imageView = findViewById( resourceId );
                    imageView.setVisibility( View.VISIBLE );

                    // Hide numbers.
                    imageView.setImageResource( R.drawable.monkey_ladder_transparent );

                    // Start in the light-blue state and animate to dark-blue over 2 seconds.
                    imageView.setBackgroundResource( R.drawable.monkey_ladder_cell_display );
                    GradientDrawable bg = ( GradientDrawable ) imageView.getBackground().mutate();

                    ValueAnimator animator = ValueAnimator.ofObject( new ArgbEvaluator(), startColor, endColor );
                    animator.setDuration( 1000 );
                    animator.addUpdateListener( a -> bg.setColor( ( Integer ) a.getAnimatedValue() ) );
                    animator.start();
                } );

        // Only allow input once the transition completes.
        new Handler( Looper.getMainLooper() ).postDelayed( ( ) -> setReadyToTakeUserInput( true ), 1000 );
    }

    @Override
    public void clearHighlightedCells( ) {
        locationMapping
                .getMapping()
                .keySet()
                .stream()
                .forEach( resourceId -> clearHighlightedCell( resourceId ) );
    }


    @Override
    public void displayUserSelectionCorrectFeedback( ) {
        resultImage.setImageResource( R.drawable.monkey_ladder_check );
        updateUserInputFeedBackImage();
    }

    @Override
    public void displayUserSelectionIncorrectFeedback( ) {
        resultImage.setImageResource( R.drawable.monkey_ladder_x_error );
        updateUserInputFeedBackImage();
    }

    @Override
    public void updateUserInputFeedBackImage( ) {
        gameUpdateTimer.schedule( new TimerTask() {
            @Override
            public void run( ) {
                runOnUiThread( ( ) ->
                        resultImage.setImageResource( R.drawable.monkey_ladder_expecting_input ) );
            }
        }, ONE_TICK_IN_MILLIS );
    }

    @Override
    public void onClick( View v ) {

        if ( !isReadyForUserInput ) {
            return;
        }

        int id = v.getId();

        Location location = locationMapping.locationForResource( id )
                .orElseThrow( ( ) -> new IllegalStateException( "Unable to find the mapping for resource " +
                        "identified by id " + id ) );

        Integer resourceId = locationMapping.resourceIdForLocation( location )
                .orElseThrow( ( ) -> new IllegalStateException( "Invalid location " + location ) );

        highlightSelectedCell( resourceId );

        presenter.addSelectedLocation( location );
    }

    /**
     * Update
     * 1) score
     * 2) player lives if different
     * 3) reset progress
     *
     * @param gameState
     */
    @Override
    public void updateGameStateInUI( GameState gameState ) {
        TextView scoreText = findViewById( R.id.score );
        scoreText.setText( gameState.getScore() + "" );

        // TODO update lives
        progressBar.setProgress( 0 );
    }

    @Override
    public void setReadyToTakeUserInput( boolean isReady ) {
        isReadyForUserInput = isReady;
    }

    @Override
    public void onGameEnd( GameState gameState ) {
        new ChartActivityIntent( this )
                .addScore( gameState.getScore() )
                .addDate( new Date() )
                .startActivity();
    }

    @Override
    public void updateLivesInUI( PlayerLives lives ) {
        int visibleLives;

        switch ( lives.getHealth() ) {
            case Danger:
                visibleLives = 1;
                break;
            case Warning:
                visibleLives = 2;
                break;
            case Healthy:
                visibleLives = 3;
                break;
            default:
                throw new RuntimeException( "Unable to determine the health image to be used " +
                        "for lives " + lives );
        }

        life1.setVisibility( visibleLives >= 1 ? View.VISIBLE : View.INVISIBLE );
        life2.setVisibility( visibleLives >= 2 ? View.VISIBLE : View.INVISIBLE );
        life3.setVisibility( visibleLives >= 3 ? View.VISIBLE : View.INVISIBLE );
    }

    private void initUserInterfaceElements( ) {
        progressBar = findViewById( R.id.progressBar );
        countdownText = findViewById( R.id.countdownText );
        resultImage = findViewById( R.id.userSelectionResult );
        life1 = findViewById( R.id.life1 );
        life2 = findViewById( R.id.life2 );
        life3 = findViewById( R.id.life3 );

        updateLivesInUI( PlayerLives.getDefaultStartingValue() );
        resultImage.setImageResource( R.drawable.monkey_ladder_expecting_input );
    }


    private void clearResource( Integer resourceId ) {
        ImageView imageView = findViewById( resourceId );

        imageView.setVisibility( View.INVISIBLE );
        imageView.setBackgroundResource( R.drawable.monkey_ladder_cell_display );
        imageView.setImageResource( R.drawable.monkey_ladder_transparent );
    }

    private void clearHighlightedCell( Integer resourceId ) {
        ImageView imageView = findViewById( resourceId );

        imageView.setVisibility( View.INVISIBLE );
        imageView.setBackgroundResource( R.drawable.monkey_ladder_cell_display );
        imageView.setImageResource( R.drawable.monkey_ladder_transparent );
    }

    private void highlightSelectedCell( Integer resourceId ) {
        ImageView imageView = findViewById( resourceId );

        imageView.setBackgroundResource( R.drawable.monkey_ladder_cell_active );
        imageView.setImageResource( R.drawable.monkey_ladder_transparent );
    }

    private void delayDisplayingRound( int millis ) {
        try {
            Thread.sleep( millis );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }
}