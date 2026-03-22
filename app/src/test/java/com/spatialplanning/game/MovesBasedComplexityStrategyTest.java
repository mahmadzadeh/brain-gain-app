package com.spatialplanning.game;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MovesBasedComplexityStrategyTest {

    @Test
    public void pointsGrowNonLinearly() {
        MovesBasedComplexityStrategy strategy = new MovesBasedComplexityStrategy();

        assertEquals(2, strategy.forRound(1).points());
        assertEquals(3, strategy.forRound(2).points());
        assertEquals(5, strategy.forRound(3).points());
        assertEquals(8, strategy.forRound(4).points());
    }

    @Test
    public void solveMoveTargetIncreasesByRound() {
        MovesBasedComplexityStrategy strategy = new MovesBasedComplexityStrategy();

        assertEquals(2, strategy.forRound(1).solveMovesTarget());
        assertEquals(3, strategy.forRound(2).solveMovesTarget());
        assertEquals(6, strategy.forRound(5).solveMovesTarget());
    }
}
