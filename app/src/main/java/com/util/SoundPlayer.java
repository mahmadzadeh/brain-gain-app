package com.util;


import android.content.Context;
import android.media.MediaPlayer;

import com.monkeyladder.R;

public class SoundPlayer {

    private final MediaPlayer buzzer;
    private final MediaPlayer ding;

    public SoundPlayer( MediaPlayer buzzer, MediaPlayer ding ) {
        this.buzzer = buzzer;
        this.ding = ding;
    }

    public SoundPlayer( Context context ) {
        this.buzzer = MediaPlayer.create( context, R.raw.fail );
        this.ding = MediaPlayer.create( context, R.raw.ding );
    }

    public void soundFeedbackForUserInput( boolean givenCorrectAnswer ) {
        if ( givenCorrectAnswer ) {
            safeStart( ding );
        } else {
            safeStart( buzzer );
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
