package com.tokensearch.game;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenSearchGameTest {

    private TokenSearchGame game;

    @Before
    public void setUp() {
        Random random = new Random( 42 );
        BoxLayout layout = new BoxLayout( random );
        game = new TokenSearchGame( layout, random );
    }

    @Test
    public void initialStateHasThreeLives() {
        assertThat( game.lives(), is( 3 ) );
    }

    @Test
    public void initialStateHasZeroScore() {
        assertThat( game.score(), is( 0 ) );
    }

    @Test
    public void initialLevelIsOne() {
        assertThat( game.level(), is( 1 ) );
    }

    @Test
    public void initialBoxCountIsThree() {
        assertThat( game.boxCount(), is( 3 ) );
    }

    @Test
    public void searchingEmptyBoxReturnsEmpty() {
        // Search all boxes; at least one should be empty
        boolean foundEmpty = false;
        for ( int i = 0; i < game.boxCount(); i++ ) {
            SearchResult result = game.search( i );
            if ( result instanceof Empty ) {
                foundEmpty = true;
                break;
            }
        }
        assertTrue( "Should find at least one empty box", foundEmpty );
    }

    @Test
    public void findingTokenDoesNotChangeScore() {
        int tokenIndex = findTokenIndex();
        game.search( tokenIndex );

        assertThat( game.score(), is( 0 ) );
    }

    @Test
    public void searchingSameBoxTwiceReturnsAlreadySearched() {
        int emptyIndex = findEmptyIndex();
        game.search( emptyIndex );

        SearchResult result = game.search( emptyIndex );
        assertTrue( result instanceof AlreadySearched );
    }

    @Test
    public void searchingFoundBoxReturnsAlreadyFound() {
        int tokenIndex = findTokenIndex();
        game.search( tokenIndex );

        SearchResult result = game.search( tokenIndex );
        assertTrue( result instanceof AlreadyFound );
    }

    @Test
    public void loseLifeDecrementsLives() {
        game.loseLife();

        assertThat( game.lives(), is( 2 ) );
    }

    @Test
    public void gameOverWhenAllLivesLost() {
        game.loseLife();
        game.loseLife();
        game.loseLife();

        assertTrue( game.isGameOver() );
    }

    @Test
    public void notGameOverWithLivesRemaining() {
        game.loseLife();

        assertFalse( game.isGameOver() );
    }

    @Test
    public void tokensRemainingStartsAtBoxCount() {
        assertThat( game.tokensRemaining(), is( 3 ) );
    }

    @Test
    public void findingTokenDecreasesTokensRemaining() {
        int tokenIndex = findTokenIndex();
        game.search( tokenIndex );

        assertThat( game.tokensRemaining(), is( 2 ) );
    }

    @Test
    public void levelCompleteWhenAllTokensFound() {
        collectAllTokens();

        assertTrue( game.isLevelComplete() );
    }

    @Test
    public void advanceLevelIncrementsLevel() {
        collectAllTokens();
        game.advanceLevel();

        assertThat( game.level(), is( 2 ) );
    }

    @Test
    public void advanceLevelSetsScoreToBoxCount() {
        collectAllTokens();
        game.advanceLevel();

        assertThat( game.score(), is( 3 ) );
    }

    @Test
    public void advanceLevelIncreasesBoxCount() {
        collectAllTokens();
        game.advanceLevel();

        assertThat( game.boxCount(), is( 4 ) );
    }

    @Test
    public void resetPuzzleKeepsLevelAndScore() {
        collectAllTokens();
        game.advanceLevel();
        int scoreBeforeReset = game.score();

        game.resetPuzzle();

        assertThat( game.score(), is( scoreBeforeReset ) );
        assertThat( game.level(), is( 2 ) );
    }

    private int findTokenIndex() {
        // Create a probe game with the same seed to find the token index
        Random probeRandom = new Random( 42 );
        BoxLayout probeLayout = new BoxLayout( probeRandom );
        TokenSearchGame probe = new TokenSearchGame( probeLayout, probeRandom );
        for ( int i = 0; i < probe.boxCount(); i++ ) {
            SearchResult result = probe.search( i );
            if ( result instanceof TokenFound ) {
                return i;
            }
        }
        throw new IllegalStateException( "No token found" );
    }

    private int findEmptyIndex() {
        Random probeRandom = new Random( 42 );
        BoxLayout probeLayout = new BoxLayout( probeRandom );
        TokenSearchGame probe = new TokenSearchGame( probeLayout, probeRandom );
        for ( int i = 0; i < probe.boxCount(); i++ ) {
            SearchResult result = probe.search( i );
            if ( result instanceof Empty ) {
                return i;
            }
        }
        throw new IllegalStateException( "No empty box found" );
    }

    private void collectAllTokens() {
        for ( int round = 0; round < game.boxCount(); round++ ) {
            for ( int i = 0; i < game.boxCount(); i++ ) {
                SearchResult result = game.search( i );
                if ( result instanceof TokenFound ) {
                    break;
                }
            }
        }
    }
}
