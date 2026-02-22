package com.sudoku.game;

/**
 * Represents the complete solution of a Sudoku puzzle.
 * Provides the ability to verify whether a partial board is on the correct path.
 */
public class SudokuSolution {

    private final int[][] solution;

    public SudokuSolution( int[][] solvedCells ) {
        this.solution = new int[SudokuBoard.SIZE][SudokuBoard.SIZE];
        for ( int r = 0; r < SudokuBoard.SIZE; r++ ) {
            System.arraycopy( solvedCells[r], 0, this.solution[r], 0, SudokuBoard.SIZE );
        }
    }

    /**
     * Checks whether every non-empty user cell on the board matches the solution.
     * Empty cells are ignored — only filled-in values are validated.
     */
    public boolean isPartialSolutionCorrect( SudokuBoard board ) {
        for ( int r = 0; r < SudokuBoard.SIZE; r++ ) {
            for ( int c = 0; c < SudokuBoard.SIZE; c++ ) {
                int value = board.getValue( r, c );
                if ( value != SudokuBoard.EMPTY && value != solution[r][c] ) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getValue( int row, int col ) {
        return solution[row][col];
    }
}
