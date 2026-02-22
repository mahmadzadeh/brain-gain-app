package com.tokensearch.game;

/**
 * A box at a grid cell. Tracks whether a token has been collected here.
 */
public class Box {

    private final int row;
    private final int col;
    private boolean tokenFound;

    public Box( int row, int col ) {
        this.row = row;
        this.col = col;
        this.tokenFound = false;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    public boolean isTokenFound() {
        return tokenFound;
    }

    public void markTokenFound() {
        this.tokenFound = true;
    }
}
