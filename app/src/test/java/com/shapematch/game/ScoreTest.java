package com.shapematch.game;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScoreTest {

    @Test
    public void givenCurrentScoreWhenCorrectAnswerGivenThenPointsAddedToCurrentScore(){
        Score score = new Score(10);
        assertEquals( 20, score.add(10).points());
    }

    @Test
    public void givenCurrentScoreWhenIncorrectAnswerGivenThenPointsDeducted() {
        Score score = new Score(10);

        assertEquals(0, score.deduct(10).points());
    }

    @Test
    public void givenInitialScoreItNeverChanges() {
        Score score = Score.initialScore;

        assertEquals(-10, score.deduct(10).points());

        assertEquals(0 , score.points());
    }
}
