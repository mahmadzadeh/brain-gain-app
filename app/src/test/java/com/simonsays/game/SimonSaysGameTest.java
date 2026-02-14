package com.simonsays.game;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class SimonSaysGameTest {

    private SimonSaysGame game;
    private Sequence sequence;

    @Before
    public void setUp() {
        // Seeded random for deterministic tests
        sequence = new Sequence( 16, new Random( 42 ) );
        game = new SimonSaysGame( sequence, 3 );
    }

    @Test
    public void initialStateIsShowingSequence() {
        assertEquals( GamePhase.SHOWING_SEQUENCE, game.getPhase() );
    }

    @Test
    public void initialScoreIsZero() {
        assertEquals( 0, game.getScore() );
    }

    @Test
    public void initialLivesMatchConstructorArg() {
        assertEquals( 3, game.getLives() );
    }

    @Test
    public void startNewRoundExtendsSequence() {
        game.startNewRound();
        assertEquals( 1, game.getCurrentRound() );

        game.startNewRound();
        assertEquals( 2, game.getCurrentRound() );
    }

    @Test
    public void startNewRoundSetsPhaseToShowingSequence() {
        game.startNewRound();
        game.onSequenceDisplayed();
        assertEquals( GamePhase.PLAYER_INPUT, game.getPhase() );

        game.startNewRound();
        assertEquals( GamePhase.SHOWING_SEQUENCE, game.getPhase() );
    }

    @Test
    public void onSequenceDisplayedSetsPhaseToPlayerInput() {
        game.startNewRound();
        game.onSequenceDisplayed();
        assertEquals( GamePhase.PLAYER_INPUT, game.getPhase() );
    }

    @Test
    public void correctTapOnSingleTileRoundWins() {
        game.startNewRound();
        game.onSequenceDisplayed();

        int expected = sequence.getPosition( 0 );
        GamePhase result = game.handlePlayerTap( expected );
        assertEquals( GamePhase.ROUND_WON, result );
    }

    @Test
    public void scoreUpdatesOnRoundWon() {
        game.startNewRound();
        game.onSequenceDisplayed();

        int expected = sequence.getPosition( 0 );
        game.handlePlayerTap( expected );
        assertEquals( 1, game.getScore() );
    }

    @Test
    public void wrongTapDecrementsLives() {
        game.startNewRound();
        game.onSequenceDisplayed();

        int expected = sequence.getPosition( 0 );
        int wrong = ( expected + 1 ) % 16;
        game.handlePlayerTap( wrong );

        assertEquals( 2, game.getLives() );
    }

    @Test
    public void wrongTapReturnsLifeLost() {
        game.startNewRound();
        game.onSequenceDisplayed();

        int expected = sequence.getPosition( 0 );
        int wrong = ( expected + 1 ) % 16;
        GamePhase result = game.handlePlayerTap( wrong );

        assertEquals( GamePhase.LIFE_LOST, result );
    }

    @Test
    public void gameOverWhenAllLivesLost() {
        SimonSaysGame oneLifeGame = new SimonSaysGame(
                new Sequence( 16, new Random( 42 ) ), 1 );
        oneLifeGame.startNewRound();
        oneLifeGame.onSequenceDisplayed();

        int expected = oneLifeGame.getSequence().getPosition( 0 );
        int wrong = ( expected + 1 ) % 16;
        GamePhase result = oneLifeGame.handlePlayerTap( wrong );

        assertEquals( GamePhase.GAME_OVER, result );
        assertEquals( 0, oneLifeGame.getLives() );
    }

    @Test
    public void tapIgnoredDuringShowingSequence() {
        game.startNewRound();
        // Don't call onSequenceDisplayed — still showing

        GamePhase result = game.handlePlayerTap( 0 );
        assertEquals( GamePhase.SHOWING_SEQUENCE, result );
        assertEquals( 3, game.getLives() );
    }

    @Test
    public void multiTapSequenceCompletesRound() {
        // Build a two-tile round
        game.startNewRound();
        game.onSequenceDisplayed();
        int first = sequence.getPosition( 0 );
        game.handlePlayerTap( first );
        // Round won after single tile, now start round 2
        game.startNewRound();
        game.onSequenceDisplayed();

        // Must tap both tiles correctly
        int tile0 = sequence.getPosition( 0 );
        int tile1 = sequence.getPosition( 1 );
        GamePhase afterFirst = game.handlePlayerTap( tile0 );
        assertEquals( GamePhase.PLAYER_INPUT, afterFirst );

        GamePhase afterSecond = game.handlePlayerTap( tile1 );
        assertEquals( GamePhase.ROUND_WON, afterSecond );
        assertEquals( 2, game.getScore() );
    }

    @Test
    public void lifeLostResetsInputIndex() {
        game.startNewRound();
        game.startNewRound();
        game.onSequenceDisplayed();

        // Tap wrong
        int expected = sequence.getPosition( 0 );
        int wrong = ( expected + 1 ) % 16;
        game.handlePlayerTap( wrong );
        assertEquals( GamePhase.LIFE_LOST, game.getPhase() );

        // After replay, player can try again from position 0
        game.onSequenceDisplayed();
        GamePhase result = game.handlePlayerTap( sequence.getPosition( 0 ) );
        // If sequence has 2 tiles, this is just the first correct tap
        assertEquals( GamePhase.PLAYER_INPUT, result );
    }

    @Test
    public void scoreReflectsLongestSequence() {
        // Complete round 1 (1 tile)
        game.startNewRound();
        game.onSequenceDisplayed();
        game.handlePlayerTap( sequence.getPosition( 0 ) );
        assertEquals( 1, game.getScore() );

        // Complete round 2 (2 tiles)
        game.startNewRound();
        game.onSequenceDisplayed();
        game.handlePlayerTap( sequence.getPosition( 0 ) );
        game.handlePlayerTap( sequence.getPosition( 1 ) );
        assertEquals( 2, game.getScore() );
    }
}
