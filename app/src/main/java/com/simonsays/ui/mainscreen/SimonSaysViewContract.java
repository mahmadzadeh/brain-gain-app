package com.simonsays.ui.mainscreen;

public interface SimonSaysViewContract {

    void flashTile( int tileIndex );

    void resetTile( int tileIndex );

    void updateScore( int score );

    void updateLives( int lives );

    void updateRound( int round );

    void setStatusText( String text );

    void setTilesEnabled( boolean enabled );

    void onGameOver( int finalScore );
}
