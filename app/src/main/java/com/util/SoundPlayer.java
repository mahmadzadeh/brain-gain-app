package com.util;


import android.content.Context;
import android.media.MediaPlayer;

import com.monkeyladder.R;

public class SoundPlayer {

    private final MediaPlayer buzzer;
    private final MediaPlayer ding;
    private final MediaPlayer over;

    public SoundPlayer( MediaPlayer buzzer, MediaPlayer ding, MediaPlayer over ) {
        this.buzzer = buzzer;
        this.ding = ding;
        this.over = over;
    }

    public SoundPlayer( Context context ) {
        this.buzzer = MediaPlayer.create( context, R.raw.over );
        this.ding = MediaPlayer.create( context, R.raw.ding );
        this.over = MediaPlayer.create( context, R.raw.over );
    }

    public void soundFeedbackForUserInput( boolean givenCorrectAnswer ) {
        if ( givenCorrectAnswer ) {
            playDing();
        } else {
            playBuzzer();
        }
    }

    public void playDing() {
        safeStart( ding );
    }

    public void playBuzzer() {
        safeStart( buzzer );
    }

    public void playOver() {
        safeStart( over );
    }

    public void release() {
        safeRelease( buzzer );
        safeRelease( ding );
        safeRelease( over );
    }

    private void safeRelease( MediaPlayer player ) {
        if ( player != null ) {
            player.release();
        }
    }

    private void safeStart( MediaPlayer player ) {
        if ( player == null ) {
            return;
        }

        try {
            player.seekTo( 0 );
            player.start();
        } catch ( IllegalStateException ignored ) {
            // No-op
        }
    }
}
