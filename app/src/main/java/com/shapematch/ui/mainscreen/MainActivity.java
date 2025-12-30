package com.shapematch.ui.mainscreen;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.util.DateUtil;
import com.shapematch.game.Cell;
import com.shapematch.game.CellGrid;
import com.shapematch.game.CellGridPair;
import com.shapematch.game.CellGridUtil;
import com.shapematch.game.GameLevel;

import java.io.File;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

public class MainActivity extends AppCompatActivity implements MainScreenView {

    private MainActivityPresenter presenter;
    private ImageView feedbackImage;
    private ImageView feedbackWrongImage;
    private TextView scoreText;
    private TextView levelText;
    private TextView timerText;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_shape_match_main );

        // Initialize presenter
        presenter = new MainActivityPresenter( this );

        // Initialize views
        feedbackImage = findViewById( R.id.feedbackImage );
        feedbackWrongImage = findViewById( R.id.feedbackWrongImage );
        scoreText = findViewById( R.id.scoreText );
        levelText = findViewById( R.id.levelText );
        timerText = findViewById( R.id.timerText );
        ImageButton matchButton = findViewById( R.id.matchButton );
        ImageButton mismatchButton = findViewById( R.id.mismatchButton );

        // Wire up button click handlers
        matchButton.setOnClickListener( v -> presenter.handleMatchButtonClick() );
        mismatchButton.setOnClickListener( v -> presenter.handleMismatchButtonClick() );

        // Display initial game state
        presenter.displayCurrentState();
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

    public void displayGrids( CellGridPair gridPair ) {
        try {
            android.util.Log.d("ShapeMatch", "Displaying grids");

            // Display left grid
            List<List<Cell>> leftGrid = gridPair.leftGrid().populateGridCells();
            android.util.Log.d("ShapeMatch", "Left grid populated with " + leftGrid.size() + " rows");
            displayGrid( leftGrid, "leftCell" );

            // Display right grid (may have one shape different)
            List<List<Cell>> rightGrid = gridPair.rightGrid().populateGridCells();
            android.util.Log.d("ShapeMatch", "Right grid populated with " + rightGrid.size() + " rows");
            displayGrid( rightGrid, "rightCell" );

            android.util.Log.d("ShapeMatch", "Grids displayed successfully");
        } catch (Exception e) {
            android.util.Log.e("ShapeMatch", "Error displaying grids", e);
        }
    }

    private void displayGrid( List<List<Cell>> grid, String cellIdPrefix ) {
        int shapesDisplayed = 0;
        for ( int row = 0; row < grid.size(); row++ ) {
            List<Cell> rowCells = grid.get( row );
            for ( int col = 0; col < rowCells.size(); col++ ) {
                Cell cell = rowCells.get( col );
                String cellId = cellIdPrefix + "_" + row + "_" + col;
                int resId = getResources().getIdentifier( cellId, "id", getPackageName() );

                if ( resId != 0 ) {
                    ImageView imageView = findViewById( resId );
                    int shapeResourceId = cell.getShapeResourceId();

                    if ( shapeResourceId != 0 ) {
                        // Display raw PNG without any tinting or background
                        imageView.setImageResource( shapeResourceId );
                        ImageViewCompat.setImageTintList( imageView, null );
                        shapesDisplayed++;
                        android.util.Log.d("ShapeMatch", "Displayed shape at " + cellId + " with resource ID: " + shapeResourceId);
                    } else {
                        // Empty cell - clear any previous image
                        imageView.setImageResource( 0 );
                    }
                } else {
                    android.util.Log.e("ShapeMatch", "Could not find view ID for: " + cellId);
                }
            }
        }
        android.util.Log.d("ShapeMatch", "Total shapes displayed in " + cellIdPrefix + " grid: " + shapesDisplayed);
    }

    // MainScreenView interface implementations

    @Override
    public void showFeedback( boolean isCorrect ) {
        if ( isCorrect ) {
            feedbackWrongImage.setImageResource( R.drawable.nback_transparent );
            feedbackImage.setImageResource( R.drawable.nback_checkmark );
        } else {
            feedbackImage.setImageResource( R.drawable.nback_transparent );
            feedbackWrongImage.setImageResource( R.drawable.nback_xmark );
        }

        // Match Dual N-Back behavior: swap icon, then revert to transparent after 500ms.
        feedbackImage.postDelayed( () -> {
            feedbackImage.setImageResource( R.drawable.nback_transparent );
            feedbackWrongImage.setImageResource( R.drawable.nback_transparent );
        }, 500 );
    }

    @Override
    public void updateScore( int score ) {
        scoreText.setText( String.valueOf( score ) );
    }

    @Override
    public void updateLevel( int level ) {
        levelText.setText( String.valueOf( level ) );
    }

    @Override
    public void setCountDownText( String text ) {
        timerText.setText( text );
    }

    @Override
    public void onFinish( int finalScore ) {
        Date date = new Date();
        saveGameResult(date, finalScore);

        Intent continueIntent = new Intent(this, ContinueActivity.class);
        continueIntent.putExtra(ContinueActivity.EXTRA_TITLE, "Shape Match");
        continueIntent.putExtra(ContinueActivity.EXTRA_ICON_RES_ID, R.drawable.shape_match_icon);
        continueIntent.putExtra(ContinueActivity.EXTRA_SCORE_TEXT, "Score " + finalScore);
        continueIntent.putExtra(ContinueActivity.EXTRA_REPLAY_ACTIVITY, "com.shapematch.ui.mainscreen.MainActivity");
        continueIntent.putExtra(ContinueActivity.EXTRA_SHOW_STATS, true);
        continueIntent.putExtra(ChartActivityIntent.FINAL_SCORE, finalScore);
        continueIntent.putExtra(ChartActivityIntent.DATE, DateUtil.format(date));

        startActivity(continueIntent);
        finish();
    }

    private void saveGameResult(Date date, int score) {
        DataPoint newDataPoint = new DataPoint(date, score);
        DataPointCollection existingData = readAllData(getFilesDir());

        existingData.addDataPoint(newDataPoint);
        DataPointCollection shrunkData = existingData.shrinkDataSize();

        new FileBasedDao(
            new FileIO(
                FileUtil.getDataFile(getFilesDir(), "shape_match_scores.json")
            )
        ).write(shrunkData);
    }

    private DataPointCollection readAllData(File filesDir) {
        Dao dao = new FileBasedDao(
            new FileIO(
                FileUtil.getDataFile(filesDir, "shape_match_scores.json")
            )
        );
        return dao.read();
    }

    @Override
    public void displayNewGrids( CellGridPair gridPair ) {
        displayGrids( gridPair );
    }
}
