package com.spatialplanning.game;

/**
 * Default strategy:
 * - Complexity increases by required solve-move target each round.
 * - Points grow non-linearly (quadratic trend) to reward harder rounds.
 */
public class MovesBasedComplexityStrategy implements ComplexityStrategy {

    private static final int BASE_SOLVE_MOVES_TARGET = 2;

    @Override
    public ComplexityLevel forRound(int round) {
        int normalizedRound = Math.max(1, round);
        int solveMovesTarget = BASE_SOLVE_MOVES_TARGET + (normalizedRound - 1);

        // Non-linear points: 2, 3, 5, 8, 12, ...
        int points = 2 + ((normalizedRound - 1) * normalizedRound) / 2;
        return new ComplexityLevel(normalizedRound, solveMovesTarget, points);
    }
}
