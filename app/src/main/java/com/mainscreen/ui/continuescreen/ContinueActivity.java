package com.mainscreen.ui.continuescreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chart.ui.ChartActivity;
import com.chart.ui.ChartActivityIntent;
import com.mainscreen.ui.GameSelectionActivity;
import com.monkeyladder.R;

import androidx.appcompat.app.AppCompatActivity;

public class ContinueActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_SCORE_TEXT = "extra_score_text";
    public static final String EXTRA_ICON_RES_ID = "extra_icon_res_id";
    public static final String EXTRA_REPLAY_ACTIVITY = "extra_replay_activity";

    public static final String EXTRA_SHOW_STATS = "extra_show_stats";

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_continue_screen );

        ImageView icon = findViewById( R.id.continueIcon );
        TextView title = findViewById( R.id.continueTitle );
        TextView score = findViewById( R.id.continueScore );

        Button playAgain = findViewById( R.id.continuePlayAgain );
        Button chooseGame = findViewById( R.id.continueChooseGame );
        Button viewStats = findViewById( R.id.continueViewStats );

        Intent intent = getIntent();

        int iconResId = intent.getIntExtra( EXTRA_ICON_RES_ID, 0 );
        if ( iconResId != 0 ) {
            icon.setImageResource( iconResId );
        } else {
            icon.setVisibility( View.GONE );
        }

        String titleText = intent.getStringExtra( EXTRA_TITLE );
        if ( titleText != null ) {
            title.setText( titleText );
        }

        String scoreText = intent.getStringExtra( EXTRA_SCORE_TEXT );
        if ( scoreText != null ) {
            score.setText( scoreText );
        }

        String replayActivityName = intent.getStringExtra( EXTRA_REPLAY_ACTIVITY );
        playAgain.setOnClickListener( v -> startByNameOrFinish( replayActivityName ) );

        chooseGame.setOnClickListener( v -> {
            Intent i = new Intent( ContinueActivity.this, GameSelectionActivity.class );
            i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity( i );
            finish();
        } );

        boolean showStats = intent.getBooleanExtra( EXTRA_SHOW_STATS, false );
        if ( showStats && intent.getExtras() != null &&
                intent.getExtras().containsKey( ChartActivityIntent.FINAL_SCORE ) &&
                intent.getExtras().containsKey( ChartActivityIntent.DATE ) ) {

            viewStats.setOnClickListener( v -> {
                Intent statsIntent = new Intent( ContinueActivity.this, ChartActivity.class );
                statsIntent.putExtras( intent.getExtras() );
                startActivity( statsIntent );
            } );
        } else {
            viewStats.setVisibility( View.GONE );
        }
    }

    private void startByNameOrFinish( String activityClassName ) {
        if ( activityClassName == null ) {
            finish();
            return;
        }
        try {
            Class<?> clazz = Class.forName( activityClassName );
            startActivity( new Intent( this, clazz ) );
        } catch ( ClassNotFoundException e ) {
            throw new RuntimeException( e );
        }
        finish();
    }
}
