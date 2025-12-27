package com.mainscreen.ui;

import android.os.Bundle;
import android.view.View;

import com.monkeyladder.R;
import com.mainscreen.ui.countdown.CountdownActivityIntent;
import com.stroop.ui.countdown.CountDownActivityIntent;

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
            new CountDownActivityIntent( this ).startActivity();
        } else if ( id == R.id.shapeMatchImg ) {
            throw new RuntimeException( "shapeMatchImg is not implemented" );
        } else if ( id == R.id.monkeyLadderImg ) {
            new CountdownActivityIntent(this)
                    .setTitle("Monkey Ladder")
                    .setIconResId(R.drawable.ml_icon)
                    .setSeconds(5)
                    .setTargetActivity("com.monkeyladder.ui.mainscreen.MainActivity")
                    .startActivity();
        } else if ( id == R.id.dualNBackImg ) {
            new CountdownActivityIntent(this)
                    .setTitle("Dual N-Back")
                    .setIconResId(R.drawable.dnb_icon)
                    .setSeconds(5)
                    .setTargetActivity("com.dualnback.ui.mainscreen.MainActivity")
                    .startActivity();
        } else {
            throw new RuntimeException( "" );
        }
    }
}
