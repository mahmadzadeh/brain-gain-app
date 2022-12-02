package com.mainscreen.ui;

import android.os.Bundle;
import android.view.View;

import com.monkeyladder.R;
import com.stroop.ui.countdown.CountDownActivityIntent;

import androidx.appcompat.app.AppCompatActivity;

public class LandingScreenActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_landing_screen );
    }

    @Override
    public void onClick( View v ) {

        int id = v.getId();
        if ( id == R.id.stroopImg ) {
            new CountDownActivityIntent( this ).startActivity();
        } else if ( id == R.id.shapeMatchImg ) {
            throw new RuntimeException( "shapeMatchImg is not implemented" );
        } else if ( id == R.id.monkeyLadderImg ) {
            new com.monkeyladder.ui.mainscreen.MainActivityIntent( this ).startActivity();
        } else if ( id == R.id.dualNBackImg ) {
            new com.dualnback.ui.mainscreen.MainActivityIntent( this ).startActivity();
        } else {
            throw new RuntimeException( "" );
        }
    }
}