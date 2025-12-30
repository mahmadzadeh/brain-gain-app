package com.shapematch.ui.mainscreen;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ImageView;

import com.monkeyladder.R;
import com.shapematch.game.Cell;
import com.shapematch.game.CellGrid;
import com.shapematch.game.CellGridPair;
import com.shapematch.game.CellGridUtil;
import com.shapematch.game.GameLevel;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_shape_match_main );

        // Generate and display random shapes
        displayRandomShapes();
    }

    private void displayRandomShapes() {
        try {
            // Generate grids for level 5 (5 random shapes)
            android.util.Log.d("ShapeMatch", "Generating grid pair for level 5");
            CellGridPair gridPair = CellGridUtil.getShapesForLevel( new GameLevel( 5 ) );
            android.util.Log.d("ShapeMatch", "Grid pair generated successfully");

            // Display left grid
            List<List<Cell>> leftGrid = gridPair.leftGrid().populateGridCells();
            android.util.Log.d("ShapeMatch", "Left grid populated with " + leftGrid.size() + " rows");
            displayGrid( leftGrid, "leftCell" );

            // Display right grid (may have one shape different)
            List<List<Cell>> rightGrid = gridPair.rightGrid().populateGridCells();
            android.util.Log.d("ShapeMatch", "Right grid populated with " + rightGrid.size() + " rows");
            displayGrid( rightGrid, "rightCell" );

            android.util.Log.d("ShapeMatch", "Shapes displayed successfully");
        } catch (Exception e) {
            android.util.Log.e("ShapeMatch", "Error displaying shapes", e);
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
}
