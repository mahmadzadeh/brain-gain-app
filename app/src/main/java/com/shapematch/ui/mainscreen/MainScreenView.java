package com.shapematch.ui.mainscreen;

import com.shapematch.game.CellGridPair;

public interface MainScreenView {

    void showFeedback( boolean isCorrect );

    void updateScore( int score );

    void updateLevel( int level );

    void setCountDownText( String text );

    void onFinish( int finalScore );

    void displayNewGrids( CellGridPair gridPair );
}
