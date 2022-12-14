package com.dualnback.ui.mainscreen;

import java.io.File;

public interface MainScreenView {

    void setCountDownText( String text );

    void vibrateFor( int vibrationLength );

    void setCountDownTextAndColor( String text, int color );

    void setPositionMatchFeedBack( boolean isCorrectAnswer );

    void setSoundMatchFeedBack( boolean isCorrectAnswer );

    void updateLocationFeedBackImage( );

    void updateSoundFeedBackImage( );

    void setScoreTextRound( String text );

    void onFinish( double currentScore );

    void updateCellState( int cellToTurnOff, int offCellState );

    File getFilesDir( );
}
