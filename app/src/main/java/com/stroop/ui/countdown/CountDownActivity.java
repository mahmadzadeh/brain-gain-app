package com.stroop.ui.countdown;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.monkeyladder.R;
import com.stroop.ui.mainscreen.MainActivity;
import com.util.CountdownImageSwapHandler;
import com.util.TimerUtil;

import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class CountDownActivity extends AppCompatActivity {
    private final Long TIMER_INTERVAL = 100l;
    private final Long TIMER_DELAY = 1000l;
    ImageView countDownImage;
    CountdownImageSwapHandler handler = new CountdownImageSwapHandler( this );

    TimerTask timerTask = new TimerTask() {
        public void run( ) {
            if ( handler.hasMoreImagesToSwap() ) {
                handler.obtainMessage( 1 ).sendToTarget();
            } else {
                Intent countDownIntent = new Intent( CountDownActivity.this, MainActivity.class );
                startActivity( countDownIntent );
            }
        }
    };
    TimerUtil countDownTimer = new TimerUtil( timerTask, TIMER_INTERVAL, TIMER_DELAY );

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.countdown_screen );

        countDownImage = ( ImageView ) findViewById( R.id.imageView );

        countDownTimer.start();
    }

    public void swapImage( int resourceId ) {
        countDownImage.setImageResource( resourceId );
    }

    @Override
    public void onPause( ) {
        super.onPause();
        countDownTimer.pause();
    }

    @Override
    public void onStop( ) {
        super.onStop();
        countDownTimer.pause();
    }
}
