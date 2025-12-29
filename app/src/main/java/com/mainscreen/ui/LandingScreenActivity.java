package com.mainscreen.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;

import com.monkeyladder.R;
import com.mainscreen.ui.countdown.CountdownActivityIntent;
import com.stroop.ui.countdown.CountDownActivityIntent;

import androidx.appcompat.app.AppCompatActivity;

public class LandingScreenActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_landing_screen );

        final ProgressBar progressBar = findViewById( R.id.splashProgress );

        new CountDownTimer( 5000, 50 ) {
            @Override
            public void onTick( long millisUntilFinished ) {
                int progress = (int) ( ( 5000 - millisUntilFinished ) * 100 / 5000 );
                progressBar.setProgress( progress );
            }

            @Override
            public void onFinish() {
                progressBar.setProgress( 100 );
                startActivity( new Intent( LandingScreenActivity.this, GameSelectionActivity.class ) );
                finish();
            }
        }.start();
    }

    // Used by android:onClick on the landing start button.
    public void onStartClicked( View view ) {
        startActivity( new Intent( this, GameSelectionActivity.class ) );
    }

    @Override
    public void onClick( View v ) {

        int id = v.getId();
        if ( id == R.id.stroopImg ) {
            new CountDownActivityIntent( this ).startActivity();
        } else if ( id == R.id.shapeMatchImg ) {
            throw new RuntimeException( "shapeMatchImg is not implemented" );
        } else if ( id == R.id.monkeyLadderImg ) {
            new CountdownActivityIntent(this)
                    .setTitle("Monkey Ladder")
                    .setIconResId(R.drawable.monkey_ladder_icon)
                    .setSeconds(3)
                    .setTargetActivity("com.monkeyladder.ui.mainscreen.MainActivity")
                    .startActivity();
        } else if ( id == R.id.dualNBackImg ) {
            new com.dualnback.ui.mainscreen.MainActivityIntent( this ).startActivity();
        } else {
            throw new RuntimeException( "" );
        }
    }
}