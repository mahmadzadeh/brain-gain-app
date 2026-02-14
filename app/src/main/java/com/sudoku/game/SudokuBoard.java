package com.sudoku.game;

/**
 * Represents a 9x9 Sudoku board, tracking both fixed (given) and user-entered values.
 * A value of 0 means the cell is empty.
 */
public class SudokuBoard {

    public static final int SIZE = 9;
    public static final int BOX_SIZE = 3;
    public static final int EMPTY = 0;

    private final int[][] cells = new int[SIZE][SIZE];
    private final boolean[][] fixed = new boolean[SIZE][SIZE];

    public SudokuBoard() {}

    public SudokuBoard( int[][] initialValues ) {
        for ( int r = 0; r < SIZE; r++ ) {
            for ( int c = 0; c < SIZE; c++ ) {
                cells[r][c] = initialValues[r][c];
                fixed[r][c] = initialValues[r][c] != EMPTY;
            }
        }
    }

    public int getValue( int row, int col ) {
        return cells[row][col];
    }

    public void setValue( int row, int col, int value ) {
        if ( !fixed[row][col] ) {
            cells[row][col] = value;
        }
    }

    public void setValueForGeneration( int row, int col, int value ) {
        cells[row][col] = value;
    }

    public boolean isFixed( int row, int col ) {
        return fixed[row][col];
    }

    public void markAllNonEmptyAsFixed() {
        for ( int r = 0; r < SIZE; r++ ) {
            for ( int c = 0; c < SIZE; c++ ) {
                fixed[r][c] = cells[r][c] != EMPTY;
            }
        }
    }

    public void clearCell( int row, int col ) {
        if ( !fixed[row][col] ) {
            cells[row][col] = EMPTY;
        }
    }

    public boolean isEmpty( int row, int col ) {
        return cells[row][col] == EMPTY;
    }

    public boolean isFull() {
        for ( int r = 0; r < SIZE; r++ ) {
            for ( int c = 0; c < SIZE; c++ ) {
                if ( cells[r][c] == EMPTY ) return false;
            }
        }
        return true;
    }

    public int[][] getCells() {
        int[][] copy = new int[SIZE][SIZE];
        for ( int r = 0; r < SIZE; r++ ) {
            System.arraycopy( cells[r], 0, copy[r], 0, SIZE );
        }
        return copy;
    }
}
