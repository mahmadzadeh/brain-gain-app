package com.sudoku.game;

import org.junit.Test;

import static org.junit.Assert.*;

public class SudokuBoardTest {

    @Test
    public void newBoardIsEmpty() {
        SudokuBoard board = new SudokuBoard();
        for ( int r = 0; r < SudokuBoard.SIZE; r++ ) {
            for ( int c = 0; c < SudokuBoard.SIZE; c++ ) {
                assertEquals( SudokuBoard.EMPTY, board.getValue( r, c ) );
                assertTrue( board.isEmpty( r, c ) );
            }
        }
    }

    @Test
    public void setValueAndGetValue() {
        SudokuBoard board = new SudokuBoard();
        board.setValue( 0, 0, 5 );
        assertEquals( 5, board.getValue( 0, 0 ) );
    }

    @Test
    public void constructorWithInitialValuesMarksFixed() {
        int[][] initial = new int[9][9];
        initial[0][0] = 7;
        initial[4][4] = 3;

        SudokuBoard board = new SudokuBoard( initial );

        assertTrue( board.isFixed( 0, 0 ) );
        assertEquals( 7, board.getValue( 0, 0 ) );
        assertTrue( board.isFixed( 4, 4 ) );
        assertEquals( 3, board.getValue( 4, 4 ) );
        assertFalse( board.isFixed( 1, 1 ) );
    }

    @Test
    public void cannotOverwriteFixedCell() {
        int[][] initial = new int[9][9];
        initial[0][0] = 7;

        SudokuBoard board = new SudokuBoard( initial );
        board.setValue( 0, 0, 9 );

        assertEquals( 7, board.getValue( 0, 0 ) );
    }

    @Test
    public void canOverwriteNonFixedCell() {
        SudokuBoard board = new SudokuBoard();
        board.setValue( 2, 3, 4 );
        assertEquals( 4, board.getValue( 2, 3 ) );

        board.setValue( 2, 3, 8 );
        assertEquals( 8, board.getValue( 2, 3 ) );
    }

    @Test
    public void clearCellOnNonFixedCell() {
        SudokuBoard board = new SudokuBoard();
        board.setValue( 1, 1, 6 );
        assertFalse( board.isEmpty( 1, 1 ) );

        board.clearCell( 1, 1 );
        assertTrue( board.isEmpty( 1, 1 ) );
    }

    @Test
    public void clearCellDoesNothingOnFixedCell() {
        int[][] initial = new int[9][9];
        initial[0][0] = 5;

        SudokuBoard board = new SudokuBoard( initial );
        board.clearCell( 0, 0 );
        assertEquals( 5, board.getValue( 0, 0 ) );
    }

    @Test
    public void isFullReturnsFalseForEmptyBoard() {
        SudokuBoard board = new SudokuBoard();
        assertFalse( board.isFull() );
    }

    @Test
    public void isFullReturnsTrueWhenAllCellsFilled() {
        SudokuBoard board = new SudokuBoard();
        for ( int r = 0; r < SudokuBoard.SIZE; r++ ) {
            for ( int c = 0; c < SudokuBoard.SIZE; c++ ) {
                board.setValueForGeneration( r, c, 1 );
            }
        }
        assertTrue( board.isFull() );
    }

    @Test
    public void isFullReturnsFalseWithOneEmptyCell() {
        SudokuBoard board = new SudokuBoard();
        for ( int r = 0; r < SudokuBoard.SIZE; r++ ) {
            for ( int c = 0; c < SudokuBoard.SIZE; c++ ) {
                board.setValueForGeneration( r, c, 1 );
            }
        }
        board.setValueForGeneration( 8, 8, SudokuBoard.EMPTY );
        assertFalse( board.isFull() );
    }

    @Test
    public void markAllNonEmptyAsFixed() {
        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 0, 0, 5 );
        board.setValueForGeneration( 3, 3, 9 );

        assertFalse( board.isFixed( 0, 0 ) );
        assertFalse( board.isFixed( 3, 3 ) );

        board.markAllNonEmptyAsFixed();

        assertTrue( board.isFixed( 0, 0 ) );
        assertTrue( board.isFixed( 3, 3 ) );
        assertFalse( board.isFixed( 1, 1 ) );
    }

    @Test
    public void setValueForGenerationBypassesFixedCheck() {
        int[][] initial = new int[9][9];
        initial[0][0] = 5;
        SudokuBoard board = new SudokuBoard( initial );

        board.setValueForGeneration( 0, 0, 9 );
        assertEquals( 9, board.getValue( 0, 0 ) );
    }

    @Test
    public void getCellsReturnsDefensiveCopy() {
        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 0, 0, 5 );

        int[][] cells = board.getCells();
        cells[0][0] = 99;

        assertEquals( 5, board.getValue( 0, 0 ) );
    }

    @Test
    public void isEmptyReturnsTrueForZeroValue() {
        SudokuBoard board = new SudokuBoard();
        assertTrue( board.isEmpty( 0, 0 ) );
    }

    @Test
    public void isEmptyReturnsFalseForNonZeroValue() {
        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 0, 0, 3 );
        assertFalse( board.isEmpty( 0, 0 ) );
    }
}
