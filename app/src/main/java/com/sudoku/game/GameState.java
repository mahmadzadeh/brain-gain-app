package com.sudoku.game;

/**
 * Tracks game state: elapsed time, move count, and completion status.
 */
public class GameState {

    private long elapsedSeconds;
    private int moveCount;
    private boolean completed;

    public GameState() {
        this.elapsedSeconds = 0;
        this.moveCount = 0;
        this.completed = false;
    }

    public void incrementMoveCount() {
        moveCount++;
    }

    public void setElapsedSeconds( long seconds ) {
        this.elapsedSeconds = seconds;
    }

    public void markCompleted() {
        this.completed = true;
    }

    public long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String formatElapsedTime() {
        long minutes = elapsedSeconds / 60;
        long seconds = elapsedSeconds % 60;
        return String.format( java.util.Locale.US, "%d:%02d", minutes, seconds );
    }
}
