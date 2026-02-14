package com.sudoku.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Generates valid Sudoku puzzles algorithmically.
 * Creates a complete solved board, then removes cells to create the puzzle.
 */
public class SudokuGenerator {

    private static final int SIZE = SudokuBoard.SIZE;
    private static final int EMPTY = SudokuBoard.EMPTY;

    private final Random random = new Random();

    /**
     * Generates a puzzle with the given number of pre-filled cells.
     */
    public SudokuBoard generate( int preFilledCells ) {
        SudokuBoard board = new SudokuBoard();
        fillBoard( board, 0, 0 );
        removeCells( board, ( SIZE * SIZE ) - preFilledCells );
        board.markAllNonEmptyAsFixed();
        return board;
    }

    /**
     * Generates a puzzle with ~35 pre-filled cells (medium difficulty).
     */
    public SudokuBoard generate() {
        return generate( 35 );
    }

    private boolean fillBoard( SudokuBoard board, int row, int col ) {
        if ( row == SIZE ) return true;

        int nextRow = ( col == SIZE - 1 ) ? row + 1 : row;
        int nextCol = ( col == SIZE - 1 ) ? 0 : col + 1;

        List<Integer> numbers = shuffledNumbers();

        for ( int num : numbers ) {
            if ( isValidPlacement( board, row, col, num ) ) {
                board.setValueForGeneration( row, col, num );
                if ( fillBoard( board, nextRow, nextCol ) ) {
                    return true;
                }
                board.setValueForGeneration( row, col, EMPTY );
            }
        }
        return false;
    }

    private void removeCells( SudokuBoard board, int cellsToRemove ) {
        List<int[]> positions = new ArrayList<>();
        for ( int r = 0; r < SIZE; r++ ) {
            for ( int c = 0; c < SIZE; c++ ) {
                positions.add( new int[]{ r, c } );
            }
        }
        Collections.shuffle( positions, random );

        int removed = 0;
        for ( int[] pos : positions ) {
            if ( removed >= cellsToRemove ) break;
            board.setValueForGeneration( pos[0], pos[1], EMPTY );
            removed++;
        }
    }

    private boolean isValidPlacement( SudokuBoard board, int row, int col, int num ) {
        for ( int c = 0; c < SIZE; c++ ) {
            if ( board.getValue( row, c ) == num ) return false;
        }
        for ( int r = 0; r < SIZE; r++ ) {
            if ( board.getValue( r, col ) == num ) return false;
        }
        int boxRow = ( row / SudokuBoard.BOX_SIZE ) * SudokuBoard.BOX_SIZE;
        int boxCol = ( col / SudokuBoard.BOX_SIZE ) * SudokuBoard.BOX_SIZE;
        for ( int r = boxRow; r < boxRow + SudokuBoard.BOX_SIZE; r++ ) {
            for ( int c = boxCol; c < boxCol + SudokuBoard.BOX_SIZE; c++ ) {
                if ( board.getValue( r, c ) == num ) return false;
            }
        }
        return true;
    }

    private List<Integer> shuffledNumbers() {
        List<Integer> nums = new ArrayList<>();
        for ( int i = 1; i <= SIZE; i++ ) {
            nums.add( i );
        }
        Collections.shuffle( nums, random );
        return nums;
    }
}
