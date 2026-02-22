package com.sudoku.game;

/**
 * Holds the puzzle board and its corresponding solution,
 * as returned by {@link SudokuGenerator}.
 */
public class GeneratedPuzzle {

    private final SudokuBoard puzzle;
    private final SudokuSolution solution;

    public GeneratedPuzzle( SudokuBoard puzzle, SudokuSolution solution ) {
        this.puzzle = puzzle;
        this.solution = solution;
    }

    public SudokuBoard getPuzzle() {
        return puzzle;
    }

    public SudokuSolution getSolution() {
        return solution;
    }
}
