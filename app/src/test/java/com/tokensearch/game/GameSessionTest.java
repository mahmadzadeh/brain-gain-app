package com.tokensearch.game;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GameSessionTest {

    private GameSession session;
    private static final int INITIAL_LIVES = 3;

    @Before
    public void setUp() {
        session = new GameSession( INITIAL_LIVES );
    }

    @Test
    public void initialSessionState() {
        assertThat( session.lives(), is( INITIAL_LIVES ) );
        assertThat( session.level(), is( 1 ) );
        assertThat( session.scoreValue(), is( 0 ) );
        assertThat( session.tokensCollected(), is( 0 ) );
    }

    @Test
    public void loseLifeDecrementsLives() {
        session.loseLife();
        assertThat( session.lives(), is( INITIAL_LIVES - 1 ) );
    }

    @Test
    public void isGameOverWhenLivesAreZero() {
        session.loseLife();
        session.loseLife();
        session.loseLife();
        assertThat( session.isGameOver(), is( true ) );
    }

    @Test
    public void incrementTokensIncreasesCollectedCount() {
        session.incrementTokens( 5 );
        assertThat( session.tokensCollected(), is( 1 ) );
    }

    @Test
    public void isLevelCompleteWhenAllTokensFound() {
        session.incrementTokens( 2 );
        session.incrementTokens( 2 );
        assertThat( session.isLevelComplete( 2 ), is( true ) );
    }

    @Test
    public void advanceLevelIncrementsLevelAndResetsTokens() {
        session.incrementTokens( 3 );
        session.advanceLevel( 3 );
        
        assertThat( session.level(), is( 2 ) );
        assertThat( session.tokensCollected(), is( 0 ) );
        assertThat( session.scoreValue(), is( 3 ) );
    }

    @Test
    public void resetLevelProgressClearsTokensButKeepsScore() {
        session.incrementTokens( 3 );
        session.resetLevelProgress();
        
        assertThat( session.tokensCollected(), is( 0 ) );
        assertThat( session.scoreValue(), is( 0 ) );
    }
}
