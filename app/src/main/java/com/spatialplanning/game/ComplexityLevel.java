package com.spatialplanning.game;

public class ComplexityLevel {

    private final int solveMovesTarget;
    private final int points;
    private final int round;

    public ComplexityLevel(int round, int solveMovesTarget, int points) {
        this.round = round;
        this.solveMovesTarget = solveMovesTarget;
        this.points = points;
    }

    public int round() {
        return round;
    }

    public int solveMovesTarget() {
        return solveMovesTarget;
    }

    public int points() {
        return points;
    }
}
