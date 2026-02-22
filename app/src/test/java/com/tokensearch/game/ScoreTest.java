package com.tokensearch.game;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ScoreTest {

    @Test
    public void initialValueIsZero() {
        Score score = new Score();

        assertThat( score.value(), is( 0 ) );
    }

    @Test
    public void recordsCompletedBoxCount() {
        Score score = new Score();

        score.onLevelCompleted( 3 );

        assertThat( score.value(), is( 3 ) );
    }

    @Test
    public void keepsHighestBoxCount() {
        Score score = new Score();

        score.onLevelCompleted( 3 );
        score.onLevelCompleted( 5 );

        assertThat( score.value(), is( 5 ) );
    }

    @Test
    public void doesNotDecrease() {
        Score score = new Score();

        score.onLevelCompleted( 5 );
        score.onLevelCompleted( 3 );

        assertThat( score.value(), is( 5 ) );
    }
}
