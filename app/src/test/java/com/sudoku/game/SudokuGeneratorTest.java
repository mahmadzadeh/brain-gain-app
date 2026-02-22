package com.sudoku.game;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SudokuGeneratorTest {

    @Test
    public void generateReturnsNonNullBoard() {
        SudokuGenerator generator = new SudokuGenerator();
        SudokuBoard board = generator.generate().getPuzzle();
        assertNotNull( board );
    }

    @Test
    public void generatedBoardHasCorrectNumberOfPreFilledCells() {
        SudokuGenerator generator = new SudokuGenerator();
        int preFilledCount = 35;
        SudokuBoard board = generator.generate( preFilledCount ).getPuzzle();

        int filled = countFilledCells( board );
        assertEquals( preFilledCount, filled );
    }

    @Test
    public void defaultGenerateHas35PreFilledCells() {
        SudokuGenerator generator = new SudokuGenerator();
        SudokuBoard board = generator.generate().getPuzzle();

        int filled = countFilledCells( board );
        assertEquals( 35, filled );
    }

    @Test
    public void generatedBoardHasNoConflicts() {
        SudokuGenerator generator = new SudokuGenerator();
        SudokuValidator validator = new SudokuValidator();
        SudokuBoard board = generator.generate().getPuzzle();

        Set<String> conflicts = validator.findConflicts( board );
        assertTrue( "Generated board should have no conflicts", conflicts.isEmpty() );
    }

    @Test
    public void generatedBoardValuesAreInValidRange() {
        SudokuGenerator generator = new SudokuGenerator();
        SudokuBoard board = generator.generate().getPuzzle();

        for ( int r = 0; r < SudokuBoard.SIZE; r++ ) {
            for ( int c = 0; c < SudokuBoard.SIZE; c++ ) {
                int val = board.getValue( r, c );
                assertTrue( "Value should be 0-9, got: " + val, val >= 0 && val <= 9 );
            }
        }
    }

    @Test
    public void allPreFilledCellsAreMarkedAsFixed() {
        SudokuGenerator generator = new SudokuGenerator();
        SudokuBoard board = generator.generate().getPuzzle();

        for ( int r = 0; r < SudokuBoard.SIZE; r++ ) {
            for ( int c = 0; c < SudokuBoard.SIZE; c++ ) {
                if ( board.getValue( r, c ) != SudokuBoard.EMPTY ) {
                    assertTrue( "Pre-filled cell should be fixed at (" + r + "," + c + ")",
                        board.isFixed( r, c ) );
                } else {
                    assertFalse( "Empty cell should not be fixed at (" + r + "," + c + ")",
                        board.isFixed( r, c ) );
                }
            }
        }
    }

    @Test
    public void eachRowHasNoDuplicatesAmongPreFilledCells() {
        SudokuGenerator generator = new SudokuGenerator();
        SudokuBoard board = generator.generate().getPuzzle();

        for ( int r = 0; r < SudokuBoard.SIZE; r++ ) {
            Set<Integer> seen = new HashSet<>();
            for ( int c = 0; c < SudokuBoard.SIZE; c++ ) {
                int val = board.getValue( r, c );
                if ( val != SudokuBoard.EMPTY ) {
                    assertTrue( "Duplicate " + val + " in row " + r, seen.add( val ) );
                }
            }
        }
    }

    @Test
    public void eachColumnHasNoDuplicatesAmongPreFilledCells() {
        SudokuGenerator generator = new SudokuGenerator();
        SudokuBoard board = generator.generate().getPuzzle();

        for ( int c = 0; c < SudokuBoard.SIZE; c++ ) {
            Set<Integer> seen = new HashSet<>();
            for ( int r = 0; r < SudokuBoard.SIZE; r++ ) {
                int val = board.getValue( r, c );
                if ( val != SudokuBoard.EMPTY ) {
                    assertTrue( "Duplicate " + val + " in col " + c, seen.add( val ) );
                }
            }
        }
    }

    @Test
    public void eachBoxHasNoDuplicatesAmongPreFilledCells() {
        SudokuGenerator generator = new SudokuGenerator();
        SudokuBoard board = generator.generate().getPuzzle();

        for ( int boxR = 0; boxR < SudokuBoard.SIZE; boxR += 3 ) {
            for ( int boxC = 0; boxC < SudokuBoard.SIZE; boxC += 3 ) {
                Set<Integer> seen = new HashSet<>();
                for ( int r = boxR; r < boxR + 3; r++ ) {
                    for ( int c = boxC; c < boxC + 3; c++ ) {
                        int val = board.getValue( r, c );
                        if ( val != SudokuBoard.EMPTY ) {
                            assertTrue( "Duplicate " + val + " in box (" + boxR + "," + boxC + ")",
                                seen.add( val ) );
                        }
                    }
                }
            }
        }
    }

    @Test
    public void multipleGenerationsProduceDifferentBoards() {
        SudokuGenerator generator = new SudokuGenerator();
        SudokuBoard board1 = generator.generate().getPuzzle();
        SudokuBoard board2 = generator.generate().getPuzzle();

        boolean different = false;
        for ( int r = 0; r < SudokuBoard.SIZE && !different; r++ ) {
            for ( int c = 0; c < SudokuBoard.SIZE && !different; c++ ) {
                if ( board1.getValue( r, c ) != board2.getValue( r, c ) ) {
                    different = true;
                }
            }
        }
        assertTrue( "Two generated boards should differ", different );
    }

    @Test
    public void generateWithCustomPreFilledCount() {
        SudokuGenerator generator = new SudokuGenerator();

        for ( int count : new int[]{ 20, 30, 45, 50 } ) {
            SudokuBoard board = generator.generate( count ).getPuzzle();
            assertEquals( "Expected " + count + " pre-filled cells",
                count, countFilledCells( board ) );
        }
    }

    @Test
    public void generatedSolutionIsComplete() {
        SudokuGenerator generator = new SudokuGenerator();
        GeneratedPuzzle generated = generator.generate();
        SudokuSolution solution = generated.getSolution();

        for ( int r = 0; r < SudokuBoard.SIZE; r++ ) {
            for ( int c = 0; c < SudokuBoard.SIZE; c++ ) {
                int val = solution.getValue( r, c );
                assertTrue( "Solution cell should be 1-9 at (" + r + "," + c + ")",
                    val >= 1 && val <= 9 );
            }
        }
    }

    @Test
    public void puzzleMatchesSolution() {
        SudokuGenerator generator = new SudokuGenerator();
        GeneratedPuzzle generated = generator.generate();
        SudokuBoard board = generated.getPuzzle();
        SudokuSolution solution = generated.getSolution();

        assertTrue( solution.isPartialSolutionCorrect( board ) );
    }

    private int countFilledCells( SudokuBoard board ) {
        int count = 0;
        for ( int r = 0; r < SudokuBoard.SIZE; r++ ) {
            for ( int c = 0; c < SudokuBoard.SIZE; c++ ) {
                if ( board.getValue( r, c ) != SudokuBoard.EMPTY ) count++;
            }
        }
        return count;
    }
}
