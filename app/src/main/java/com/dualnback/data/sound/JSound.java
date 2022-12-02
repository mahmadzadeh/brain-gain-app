package com.dualnback.data.sound;

import android.content.Context;
import android.media.MediaPlayer;

import com.monkeyladder.R;

public class JSound extends Sound {

    public JSound( Context context ) {
        soundResource = R.raw.j;
        mediaPlayer = MediaPlayer.create( context, R.raw.j );
    }

    public JSound( int resource ) {
        soundResource = resource;
    }

    @Override
    int getSoundResource( ) {
        return soundResource;
    }
}
