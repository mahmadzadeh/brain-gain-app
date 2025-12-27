package com.mainscreen.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.monkeyladder.R;
import com.mainscreen.ui.countdown.CountdownActivityIntent;
import com.stroop.ui.countdown.CountDownActivityIntent;

import androidx.appcompat.app.AppCompatActivity;

public class LandingScreenActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_landing_screen );
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
                    .setIconResId(R.drawable.ml_icon)
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