package com.sudoku.ui.mainscreen;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chart.filesystem.dao.GameKey;
import com.chart.filesystem.dao.GameStatsRepository;
import com.chart.ui.ChartActivityIntent;
import com.mainscreen.ui.continuescreen.ContinueActivity;
import com.monkeyladder.R;
import com.sudoku.game.GameState;
import com.sudoku.game.GeneratedPuzzle;
import com.sudoku.game.SudokuBoard;
import com.sudoku.game.SudokuGenerator;
import com.sudoku.game.SudokuValidator;
import com.util.DateUtil;

import java.util.Date;
import java.util.Set;

public class SudokuMainActivity extends AppCompatActivity implements SudokuMainViewContract {

    private static final int SIZE = SudokuBoard.SIZE;

    private SudokuPresenter presenter;
    private final TextView[][] cellViews = new TextView[SIZE][SIZE];
    private View gridBorder;

    private int previousSelectedRow = -1;
    private int previousSelectedCol = -1;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sudoku_main );

        gridBorder = findViewById( R.id.gridBorder );
        initCellViews();
        initNumPad();

        GeneratedPuzzle generated = new SudokuGenerator().generate();

        presenter = new SudokuPresenter(
            this,
            generated.getPuzzle(),
            generated.getSolution(),
            new SudokuValidator(),
            new GameState()
        );

        presenter.displayBoard();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.pauseTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stopTimer();
    }

    private void initCellViews() {
        for ( int r = 0; r < SIZE; r++ ) {
            for ( int c = 0; c < SIZE; c++ ) {
                String cellId = "cell_" + r + "_" + c;
                int resId = getResources().getIdentifier( cellId, "id", getPackageName() );
                cellViews[r][c] = findViewById( resId );

                final int row = r;
                final int col = c;
                cellViews[r][c].setOnClickListener( v -> presenter.onCellSelected( row, col ) );
            }
        }
    }

    private void initNumPad() {
        for ( int i = 1; i <= 9; i++ ) {
            String padId = "numPad_" + i;
            int resId = getResources().getIdentifier( padId, "id", getPackageName() );
            TextView numButton = findViewById( resId );

            final int number = i;
            numButton.setOnClickListener( v -> presenter.onNumberSelected( number ) );
        }

        findViewById( R.id.numPad_clear ).setOnClickListener( v -> presenter.onClearSelected() );
        findViewById( R.id.undoButton ).setOnClickListener( v -> presenter.onUndo() );
        findViewById( R.id.checkButton ).setOnClickListener( v -> presenter.onCheckProgress() );
    }

    // SudokuMainViewContract implementations

    @Override
    public void updateCell( int row, int col, String value, boolean isFixed ) {
        runOnUiThread( () -> {
            TextView cell = cellViews[row][col];
            cell.setText( value );
            if ( isFixed ) {
                cell.setBackgroundResource( R.drawable.sudoku_cell_bg_fixed );
                cell.setAlpha( 1.0f );
            } else {
                cell.setBackgroundResource( R.drawable.sudoku_cell_bg );
                cell.setAlpha( 0.85f );
            }
        } );
    }

    @Override
    public void highlightSelectedCell( int row, int col ) {
        runOnUiThread( () -> {
            // Restore previous selection
            if ( previousSelectedRow >= 0 && previousSelectedCol >= 0 ) {
                cellViews[previousSelectedRow][previousSelectedCol]
                    .setBackgroundResource( R.drawable.sudoku_cell_bg );
            }
            cellViews[row][col].setBackgroundResource( R.drawable.sudoku_cell_bg_selected );
            previousSelectedRow = row;
            previousSelectedCol = col;
        } );
    }

    @Override
    public void showConflicts( Set<String> conflictKeys ) {
        runOnUiThread( () -> {
            for ( String key : conflictKeys ) {
                String[] parts = key.split( "," );
                int r = Integer.parseInt( parts[0] );
                int c = Integer.parseInt( parts[1] );
                cellViews[r][c].setBackgroundResource( R.drawable.sudoku_cell_bg_conflict );
            }
        } );
    }

    @Override
    public void clearConflicts() {
        runOnUiThread( () -> {
            for ( int r = 0; r < SIZE; r++ ) {
                for ( int c = 0; c < SIZE; c++ ) {
                    // Re-apply correct background based on state
                    if ( r == previousSelectedRow && c == previousSelectedCol ) {
                        cellViews[r][c].setBackgroundResource( R.drawable.sudoku_cell_bg_selected );
                    } else if ( cellViews[r][c].getAlpha() == 1.0f ) {
                        cellViews[r][c].setBackgroundResource( R.drawable.sudoku_cell_bg_fixed );
                    } else {
                        cellViews[r][c].setBackgroundResource( R.drawable.sudoku_cell_bg );
                    }
                }
            }
        } );
    }

    @Override
    public void updateTimer( String time ) {
        runOnUiThread( () -> {
            TextView timerText = findViewById( R.id.timerText );
            timerText.setText( time );
        } );
    }

    @Override
    public void updateMoves( int moves ) {
        runOnUiThread( () -> {
            TextView movesText = findViewById( R.id.movesText );
            movesText.setText( String.valueOf( moves ) );
        } );
    }

    @Override
    public void onPuzzleSolved( long elapsedSeconds, int moves ) {
        runOnUiThread( () -> {
            Date date = new Date();
            int score = (int) elapsedSeconds;
            saveGameResult( date, score );

            Intent continueIntent = new Intent( this, ContinueActivity.class );
            continueIntent.putExtra( ContinueActivity.EXTRA_TITLE, "Sudoku" );
            continueIntent.putExtra( ContinueActivity.EXTRA_ICON_RES_ID, R.drawable.sudoku_icon );
            continueIntent.putExtra( ContinueActivity.EXTRA_SCORE_TEXT,
                "Completed in " + formatTime( elapsedSeconds ) + " (" + moves + " moves)" );
            continueIntent.putExtra( ContinueActivity.EXTRA_REPLAY_ACTIVITY,
                "com.sudoku.ui.mainscreen.SudokuMainActivity" );
            continueIntent.putExtra( ContinueActivity.EXTRA_SHOW_STATS, true );
            continueIntent.putExtra( ContinueActivity.EXTRA_GAME_KEY, GameKey.SUDOKU.name() );
            continueIntent.putExtra( ChartActivityIntent.FINAL_SCORE, score );
            continueIntent.putExtra( ChartActivityIntent.DATE, DateUtil.format( date ) );

            startActivity( continueIntent );
            finish();
        } );
    }

    @Override
    public void flashGridBorder( boolean correct ) {
        runOnUiThread( () -> {
            int flashColor = correct ? Color.parseColor( "#4CAF50" ) : Color.parseColor( "#F44336" );
            int normalColor = androidx.core.content.ContextCompat.getColor( this, R.color.colorPrimaryDark );

            // repeatCount = 5 → normal→flash, flash→normal, normal→flash, flash→normal, normal→flash, flash→normal = 3 full flashes
            ValueAnimator animator = ValueAnimator.ofObject( new ArgbEvaluator(), normalColor, flashColor );
            animator.setDuration( 300 );
            animator.setRepeatCount( 5 );
            animator.setRepeatMode( ValueAnimator.REVERSE );
            animator.addUpdateListener( animation -> {
                int color = (int) animation.getAnimatedValue();
                GradientDrawable border = (GradientDrawable) gridBorder.getBackground();
                border.setStroke( (int) ( 3 * getResources().getDisplayMetrics().density ), color );
            } );
            animator.start();
        } );
    }

    private String formatTime( long seconds ) {
        long mins = seconds / 60;
        long secs = seconds % 60;
        return String.format( java.util.Locale.US, "%d:%02d", mins, secs );
    }

    private void saveGameResult( Date date, int score ) {
        GameStatsRepository.create( getFilesDir() ).addScore( GameKey.SUDOKU, date, score );
    }
}
