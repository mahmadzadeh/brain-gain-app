package com.monkeyladder.ui.continuescreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.monkeyladder.R;
import com.monkeyladder.ui.mainscreen.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

import static com.monkeyladder.util.IntentUtility.extractFromExtrasWithDefault;


public class ContinueScreenActivity extends AppCompatActivity {

    public static final String DATE = "GAME_DATE";
    public static final String SCORE_TEXT = "Score ";
    public static final String FINAL_SCORE = "FINAL_SCORE";

    private static String formatScore( double number ) {
        return ( ( int ) Math.round( number ) ) + "%";
    }

    public static void backToStartScreen( View view, Activity activity ) {
        Intent startScreenIntent = new Intent( view.getContext(), MainActivity.class );

        startScreenIntent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );

        activity.startActivity( startScreenIntent );
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.monkey_ladder_continue_screen );

        final Button saveButton = findViewById( R.id.saveButton );
        final Button continueButton = findViewById( R.id.continueButton );
        final Button quitButton = findViewById( R.id.quitButton );


        final TextView scoreTextView = findViewById( R.id.score );

        int score = extractFromExtrasWithDefault( getIntent().getExtras(), FINAL_SCORE, 1 );

        final String actualScore = formatScore( score );

        scoreTextView.setText( SCORE_TEXT + actualScore );

        continueButton.setOnClickListener(
                view -> backToStartScreen( view, ContinueScreenActivity.this ) );

        quitButton.setOnClickListener(
                view -> backToStartScreen( view, ContinueScreenActivity.this ) );
    }
}
