package com.mainscreen.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mainscreen.ui.countdown.CountdownActivityIntent;
import com.monkeyladder.R;

public class GameSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_game_selection );
    }

    public void onClick( View v ) {

        int id = v.getId();
        if ( id == R.id.stroopImg ) {
            new CountdownActivityIntent(this)
                    .setTitle("Stroop")
                    .setIconResId(R.drawable.stroop_icon)
                    .setSeconds(3)
                    .setTargetActivity("com.stroop.ui.mainscreen.MainActivity")
                    .startActivity();
        } else if ( id == R.id.shapeMatchImg ) {
            new CountdownActivityIntent(this)
                    .setTitle("Shape Match")
                    .setIconResId(R.drawable.shape_match_icon)
                    .setSeconds(3)
                    .setTargetActivity("com.shapematch.ui.mainscreen.MainActivity")
                    .startActivity();
        } else if ( id == R.id.monkeyLadderImg ) {
            new CountdownActivityIntent(this)
                    .setTitle("Monkey Ladder")
                    .setIconResId(R.drawable.monkey_ladder_icon)
                    .setSeconds(3)
                    .setTargetActivity("com.monkeyladder.ui.mainscreen.MainActivity")
                    .startActivity();
        } else if ( id == R.id.dualNBackImg ) {
            new CountdownActivityIntent(this)
                    .setTitle("Dual N-Back")
                    .setIconResId(R.drawable.dual_n_back_main_icon)
                    .setSeconds(3)
                    .setTargetActivity("com.dualnback.ui.mainscreen.MainActivity")
                    .startActivity();
        } else if ( id == R.id.sudokuImg ) {
            new CountdownActivityIntent(this)
                    .setTitle("Sudoku")
                    .setIconResId(R.drawable.sudoku_icon)
                    .setSeconds(3)
                    .setTargetActivity("com.sudoku.ui.mainscreen.SudokuMainActivity")
                    .startActivity();
        } else if ( id == R.id.simonSaysImg ) {
            new CountdownActivityIntent(this)
                    .setTitle("Token Search")
                    .setIconResId(R.drawable.tokensearch_icon)
                    .setSeconds(3)
                    .setTargetActivity("com.tokensearch.ui.mainscreen.TokenSearchMainActivity")
                    .startActivity();
        } else {
            throw new RuntimeException( "" );
        }
    }
}
