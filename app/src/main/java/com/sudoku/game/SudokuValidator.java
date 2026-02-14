package com.sudoku.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Validates a Sudoku board and identifies conflicting cells.
 */
public class SudokuValidator {

    private static final int SIZE = SudokuBoard.SIZE;
    private static final int BOX_SIZE = SudokuBoard.BOX_SIZE;
    private static final int EMPTY = SudokuBoard.EMPTY;

    /**
     * Returns a set of cell keys ("row,col") that have conflicts.
     */
    public Set<String> findConflicts( SudokuBoard board ) {
        Set<String> conflicts = new HashSet<>();

        for ( int r = 0; r < SIZE; r++ ) {
            conflicts.addAll( findDuplicates( board, rowPositions( r ) ) );
        }

        for ( int c = 0; c < SIZE; c++ ) {
            conflicts.addAll( findDuplicates( board, colPositions( c ) ) );
        }

        for ( int boxR = 0; boxR < SIZE; boxR += BOX_SIZE ) {
            for ( int boxC = 0; boxC < SIZE; boxC += BOX_SIZE ) {
                conflicts.addAll( findDuplicates( board, boxPositions( boxR, boxC ) ) );
            }
        }

        return conflicts;
    }

    /**
     * Checks if the board is completely and correctly solved.
     */
    public boolean isSolved( SudokuBoard board ) {
        return board.isFull() && findConflicts( board ).isEmpty();
    }

    /**
     * Checks if placing a value at the given position would cause a conflict.
     */
    public boolean hasConflict( SudokuBoard board, int row, int col, int value ) {
        if ( value == EMPTY ) return false;

        for ( int c = 0; c < SIZE; c++ ) {
            if ( c != col && board.getValue( row, c ) == value ) return true;
        }
        for ( int r = 0; r < SIZE; r++ ) {
            if ( r != row && board.getValue( r, col ) == value ) return true;
        }
        int boxRow = ( row / BOX_SIZE ) * BOX_SIZE;
        int boxCol = ( col / BOX_SIZE ) * BOX_SIZE;
        for ( int r = boxRow; r < boxRow + BOX_SIZE; r++ ) {
            for ( int c = boxCol; c < boxCol + BOX_SIZE; c++ ) {
                if ( r != row && c != col && board.getValue( r, c ) == value ) return true;
            }
        }
        return false;
    }

    public static String cellKey( int row, int col ) {
        return row + "," + col;
    }

    /**
     * Returns the set of cell keys whose values appear more than once
     * among the given positions.
     */
    private Set<String> findDuplicates( SudokuBoard board, int[][] positions ) {
        Map<Integer, List<String>> seen = new HashMap<>();

        for ( int[] pos : positions ) {
            int val = board.getValue( pos[0], pos[1] );
            if ( val == EMPTY ) continue;
            seen.computeIfAbsent( val, k -> new ArrayList<>() )
                .add( cellKey( pos[0], pos[1] ) );
        }

        Set<String> duplicates = new HashSet<>();
        for ( List<String> keys : seen.values() ) {
            if ( keys.size() > 1 ) {
                duplicates.addAll( keys );
            }
        }
        return duplicates;
    }

    private int[][] rowPositions( int row ) {
        int[][] pos = new int[SIZE][2];
        for ( int c = 0; c < SIZE; c++ ) {
            pos[c] = new int[]{ row, c };
        }
        return pos;
    }

    private int[][] colPositions( int col ) {
        int[][] pos = new int[SIZE][2];
        for ( int r = 0; r < SIZE; r++ ) {
            pos[r] = new int[]{ r, col };
        }
        return pos;
    }

    private int[][] boxPositions( int boxRow, int boxCol ) {
        int[][] pos = new int[SIZE][2];
        int i = 0;
        for ( int r = boxRow; r < boxRow + BOX_SIZE; r++ ) {
            for ( int c = boxCol; c < boxCol + BOX_SIZE; c++ ) {
                pos[i++] = new int[]{ r, c };
            }
        }
        return pos;
    }
}
