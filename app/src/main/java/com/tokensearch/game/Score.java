package com.tokensearch.game;

/**
 * Tracks the score for Token Search.
 * Score = highest box count the player has fully completed.
 */
public class Score {

    private int highestCompletedBoxCount;

    public Score() {
        this.highestCompletedBoxCount = 0;
    }

    public void onLevelCompleted( int boxCount ) {
        if ( boxCount > highestCompletedBoxCount ) {
            highestCompletedBoxCount = boxCount;
        }
    }

    public int value() {
        return highestCompletedBoxCount;
    }
}
