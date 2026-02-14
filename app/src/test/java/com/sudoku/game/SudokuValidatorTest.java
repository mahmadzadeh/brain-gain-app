package com.sudoku.game;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class SudokuValidatorTest {

    private final SudokuValidator validator = new SudokuValidator();

    // --- findConflicts ---

    @Test
    public void emptyBoardHasNoConflicts() {
        SudokuBoard board = new SudokuBoard();
        Set<String> conflicts = validator.findConflicts( board );
        assertTrue( conflicts.isEmpty() );
    }

    @Test
    public void boardWithNoDuplicatesHasNoConflicts() {
        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 0, 0, 1 );
        board.setValueForGeneration( 0, 1, 2 );
        board.setValueForGeneration( 1, 0, 3 );

        Set<String> conflicts = validator.findConflicts( board );
        assertTrue( conflicts.isEmpty() );
    }

    @Test
    public void duplicateInRowDetected() {
        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 0, 0, 5 );
        board.setValueForGeneration( 0, 4, 5 );

        Set<String> conflicts = validator.findConflicts( board );
        assertTrue( conflicts.contains( "0,0" ) );
        assertTrue( conflicts.contains( "0,4" ) );
    }

    @Test
    public void duplicateInColumnDetected() {
        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 0, 0, 7 );
        board.setValueForGeneration( 5, 0, 7 );

        Set<String> conflicts = validator.findConflicts( board );
        assertTrue( conflicts.contains( "0,0" ) );
        assertTrue( conflicts.contains( "5,0" ) );
    }

    @Test
    public void duplicateInBoxDetected() {
        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 0, 0, 3 );
        board.setValueForGeneration( 2, 2, 3 );

        Set<String> conflicts = validator.findConflicts( board );
        assertTrue( conflicts.contains( "0,0" ) );
        assertTrue( conflicts.contains( "2,2" ) );
    }

    @Test
    public void tripleDuplicateInRowAllMarked() {
        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 0, 0, 4 );
        board.setValueForGeneration( 0, 3, 4 );
        board.setValueForGeneration( 0, 7, 4 );

        Set<String> conflicts = validator.findConflicts( board );
        assertTrue( conflicts.contains( "0,0" ) );
        assertTrue( conflicts.contains( "0,3" ) );
        assertTrue( conflicts.contains( "0,7" ) );
    }

    @Test
    public void differentValuesInSameRowNoConflict() {
        SudokuBoard board = new SudokuBoard();
        for ( int c = 0; c < SudokuBoard.SIZE; c++ ) {
            board.setValueForGeneration( 0, c, c + 1 );
        }

        Set<String> conflicts = validator.findConflicts( board );
        assertTrue( conflicts.isEmpty() );
    }

    @Test
    public void conflictsAcrossMultipleGroups() {
        SudokuBoard board = new SudokuBoard();
        // Duplicate in row 0
        board.setValueForGeneration( 0, 0, 1 );
        board.setValueForGeneration( 0, 5, 1 );
        // Duplicate in column 8
        board.setValueForGeneration( 3, 8, 9 );
        board.setValueForGeneration( 7, 8, 9 );

        Set<String> conflicts = validator.findConflicts( board );
        assertEquals( 4, conflicts.size() );
        assertTrue( conflicts.contains( "0,0" ) );
        assertTrue( conflicts.contains( "0,5" ) );
        assertTrue( conflicts.contains( "3,8" ) );
        assertTrue( conflicts.contains( "7,8" ) );
    }

    // --- hasConflict ---

    @Test
    public void hasConflictReturnsFalseForEmptyValue() {
        SudokuBoard board = new SudokuBoard();
        assertFalse( validator.hasConflict( board, 0, 0, SudokuBoard.EMPTY ) );
    }

    @Test
    public void hasConflictReturnsFalseWhenNoConflict() {
        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 0, 1, 5 );
        assertFalse( validator.hasConflict( board, 0, 0, 3 ) );
    }

    @Test
    public void hasConflictReturnsTrueForRowConflict() {
        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 0, 5, 3 );
        assertTrue( validator.hasConflict( board, 0, 0, 3 ) );
    }

    @Test
    public void hasConflictReturnsTrueForColumnConflict() {
        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 5, 0, 3 );
        assertTrue( validator.hasConflict( board, 0, 0, 3 ) );
    }

    @Test
    public void hasConflictReturnsTrueForBoxConflict() {
        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 1, 1, 3 );
        assertTrue( validator.hasConflict( board, 0, 0, 3 ) );
    }

    @Test
    public void hasConflictIgnoresOwnPosition() {
        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 0, 0, 3 );
        // Checking the same position with the same value should not conflict with itself
        assertFalse( validator.hasConflict( board, 0, 0, 3 ) );
    }

    // --- isSolved ---

    @Test
    public void isSolvedReturnsFalseForEmptyBoard() {
        SudokuBoard board = new SudokuBoard();
        assertFalse( validator.isSolved( board ) );
    }

    @Test
    public void isSolvedReturnsFalseForPartiallyFilledBoard() {
        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 0, 0, 1 );
        assertFalse( validator.isSolved( board ) );
    }

    @Test
    public void isSolvedReturnsFalseForFullBoardWithConflicts() {
        SudokuBoard board = new SudokuBoard();
        // Fill entire board with 1s — full but invalid
        for ( int r = 0; r < SudokuBoard.SIZE; r++ ) {
            for ( int c = 0; c < SudokuBoard.SIZE; c++ ) {
                board.setValueForGeneration( r, c, 1 );
            }
        }
        assertFalse( validator.isSolved( board ) );
    }

    @Test
    public void isSolvedReturnsTrueForValidCompleteBoard() {
        // A known valid Sudoku solution
        int[][] solved = {
            { 5, 3, 4, 6, 7, 8, 9, 1, 2 },
            { 6, 7, 2, 1, 9, 5, 3, 4, 8 },
            { 1, 9, 8, 3, 4, 2, 5, 6, 7 },
            { 8, 5, 9, 7, 6, 1, 4, 2, 3 },
            { 4, 2, 6, 8, 5, 3, 7, 9, 1 },
            { 7, 1, 3, 9, 2, 4, 8, 5, 6 },
            { 9, 6, 1, 5, 3, 7, 2, 8, 4 },
            { 2, 8, 7, 4, 1, 9, 6, 3, 5 },
            { 3, 4, 5, 2, 8, 6, 1, 7, 9 }
        };
        SudokuBoard board = new SudokuBoard( solved );
        assertTrue( validator.isSolved( board ) );
    }

    // --- cellKey ---

    @Test
    public void cellKeyFormat() {
        assertEquals( "0,0", SudokuValidator.cellKey( 0, 0 ) );
        assertEquals( "8,8", SudokuValidator.cellKey( 8, 8 ) );
        assertEquals( "3,5", SudokuValidator.cellKey( 3, 5 ) );
    }

    // --- Edge cases ---

    @Test
    public void duplicateInLastBoxDetected() {
        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 6, 6, 2 );
        board.setValueForGeneration( 8, 8, 2 );

        Set<String> conflicts = validator.findConflicts( board );
        assertTrue( conflicts.contains( "6,6" ) );
        assertTrue( conflicts.contains( "8,8" ) );
    }

    @Test
    public void sameValueInDifferentBoxesSameRowConflicts() {
        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 0, 0, 6 );
        board.setValueForGeneration( 0, 8, 6 );

        Set<String> conflicts = validator.findConflicts( board );
        assertEquals( 2, conflicts.size() );
    }
}
