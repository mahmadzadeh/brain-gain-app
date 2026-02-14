package com.sudoku.game;

/**
 * Represents a single move on the Sudoku board for undo support.
 */
public class Move {

    private final int row;
    private final int col;
    private final int previousValue;
    public Move( int row, int col, int previousValue ) {
        this.row = row;
        this.col = col;
        this.previousValue = previousValue;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getPreviousValue() {
        return previousValue;
    }


}
