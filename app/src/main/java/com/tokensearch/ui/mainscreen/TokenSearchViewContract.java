package com.tokensearch.ui.mainscreen;

import com.tokensearch.game.Box;

import java.util.List;

public interface TokenSearchViewContract {

    void showBoxes( List<Box> boxes, int gridCols, int gridRows );

    void revealToken( int boxIndex );

    void hideToken( int boxIndex );

    void notifyEmptySearch();

    void showError( int boxIndex );

    void updateScore( int score );

    void updateLives( int lives );

    void updateLevel( int level );

    void setStatusText( int resId );

    void setStatusText( int resId, Object... formatArgs );

    void onLevelComplete();

    void onGameOver( int finalScore );
}
