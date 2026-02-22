package com.sudoku.game;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class SudokuSolutionTest {

    private int[][] createCompleteSolution() {
        return new int[][] {
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
    }

    @Test
    public void emptyBoardIsCorrectPartialSolution() {
        SudokuSolution solution = new SudokuSolution( createCompleteSolution() );
        SudokuBoard board = new SudokuBoard();

        assertTrue( solution.isPartialSolutionCorrect( board ) );
    }

    @Test
    public void correctPartialValuesReturnsTrue() {
        int[][] solutionCells = createCompleteSolution();
        SudokuSolution solution = new SudokuSolution( solutionCells );

        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 0, 0, 5 );
        board.setValueForGeneration( 1, 1, 7 );
        board.setValueForGeneration( 4, 4, 5 );

        assertTrue( solution.isPartialSolutionCorrect( board ) );
    }

    @Test
    public void oneWrongValueReturnsFalse() {
        int[][] solutionCells = createCompleteSolution();
        SudokuSolution solution = new SudokuSolution( solutionCells );

        SudokuBoard board = new SudokuBoard();
        board.setValueForGeneration( 0, 0, 5 );  // correct
        board.setValueForGeneration( 0, 1, 9 );  // wrong — should be 3

        assertFalse( solution.isPartialSolutionCorrect( board ) );
    }

    @Test
    public void fullCorrectBoardReturnsTrue() {
        int[][] solutionCells = createCompleteSolution();
        SudokuSolution solution = new SudokuSolution( solutionCells );
        SudokuBoard board = new SudokuBoard( solutionCells );

        assertTrue( solution.isPartialSolutionCorrect( board ) );
    }

    @Test
    public void getValueReturnsCorrectCell() {
        int[][] solutionCells = createCompleteSolution();
        SudokuSolution solution = new SudokuSolution( solutionCells );

        assertEquals( 5, solution.getValue( 0, 0 ) );
        assertEquals( 9, solution.getValue( 8, 8 ) );
        assertEquals( 1, solution.getValue( 3, 5 ) );
    }

    @Test
    public void solutionIsDefensiveCopy() {
        int[][] solutionCells = createCompleteSolution();
        SudokuSolution solution = new SudokuSolution( solutionCells );

        // Mutate original — should not affect solution
        solutionCells[0][0] = 0;
        assertEquals( 5, solution.getValue( 0, 0 ) );
    }
}
