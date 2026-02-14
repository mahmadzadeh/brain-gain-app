package com.mainscreen.ui;

import android.os.Bundle;
import android.view.View;

import com.monkeyladder.R;
import com.mainscreen.ui.countdown.CountdownActivityIntent;

import androidx.appcompat.app.AppCompatActivity;

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
                    .setSeconds(5)
                    .setTargetActivity("com.stroop.ui.mainscreen.MainActivity")
                    .startActivity();
        } else if ( id == R.id.shapeMatchImg ) {
            new CountdownActivityIntent(this)
                    .setTitle("Shape Match")
                    .setIconResId(R.drawable.shape_match_icon)
                    .setSeconds(5)
                    .setTargetActivity("com.shapematch.ui.mainscreen.MainActivity")
                    .startActivity();
        } else if ( id == R.id.monkeyLadderImg ) {
            new CountdownActivityIntent(this)
                    .setTitle("Monkey Ladder")
                    .setIconResId(R.drawable.monkey_ladder_icon)
                    .setSeconds(5)
                    .setTargetActivity("com.monkeyladder.ui.mainscreen.MainActivity")
                    .startActivity();
        } else if ( id == R.id.dualNBackImg ) {
            new CountdownActivityIntent(this)
                    .setTitle("Dual N-Back")
                    .setIconResId(R.drawable.dual_n_back_main_icon)
                    .setSeconds(5)
                    .setTargetActivity("com.dualnback.ui.mainscreen.MainActivity")
                    .startActivity();
        } else if ( id == R.id.sudokuImg ) {
            new CountdownActivityIntent(this)
                    .setTitle("Sudoku")
                    .setIconResId(R.drawable.sudoku_icon)
                    .setSeconds(5)
                    .setTargetActivity("com.sudoku.ui.mainscreen.SudokuMainActivity")
                    .startActivity();
        } else {
            throw new RuntimeException( "" );
        }
    }
}
