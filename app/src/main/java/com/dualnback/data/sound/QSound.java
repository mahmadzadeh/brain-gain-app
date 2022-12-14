package com.dualnback.data.sound;

import android.content.Context;
import android.media.MediaPlayer;

import com.monkeyladder.R;

public class QSound extends Sound {

    public QSound( Context context ) {
        soundResource = R.raw.q;
        mediaPlayer = MediaPlayer.create( context, R.raw.q );
    }

    public QSound( int resource ) {
        soundResource = resource;
    }

    @Override
    int getSoundResource( ) {
        return soundResource;
    }
}
