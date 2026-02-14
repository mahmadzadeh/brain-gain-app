package com.sudoku.game;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameStateTest {

    @Test
    public void initialStateHasZeroElapsedSeconds() {
        GameState state = new GameState();
        assertEquals( 0, state.getElapsedSeconds() );
    }

    @Test
    public void initialStateHasZeroMoveCount() {
        GameState state = new GameState();
        assertEquals( 0, state.getMoveCount() );
    }

    @Test
    public void initialStateIsNotCompleted() {
        GameState state = new GameState();
        assertFalse( state.isCompleted() );
    }

    @Test
    public void incrementMoveCount() {
        GameState state = new GameState();
        state.incrementMoveCount();
        assertEquals( 1, state.getMoveCount() );

        state.incrementMoveCount();
        state.incrementMoveCount();
        assertEquals( 3, state.getMoveCount() );
    }

    @Test
    public void setElapsedSeconds() {
        GameState state = new GameState();
        state.setElapsedSeconds( 120 );
        assertEquals( 120, state.getElapsedSeconds() );
    }

    @Test
    public void markCompleted() {
        GameState state = new GameState();
        state.markCompleted();
        assertTrue( state.isCompleted() );
    }

    @Test
    public void formatElapsedTimeZero() {
        GameState state = new GameState();
        assertEquals( "0:00", state.formatElapsedTime() );
    }

    @Test
    public void formatElapsedTimeSeconds() {
        GameState state = new GameState();
        state.setElapsedSeconds( 45 );
        assertEquals( "0:45", state.formatElapsedTime() );
    }

    @Test
    public void formatElapsedTimeMinutesAndSeconds() {
        GameState state = new GameState();
        state.setElapsedSeconds( 125 );
        assertEquals( "2:05", state.formatElapsedTime() );
    }

    @Test
    public void formatElapsedTimeExactMinutes() {
        GameState state = new GameState();
        state.setElapsedSeconds( 300 );
        assertEquals( "5:00", state.formatElapsedTime() );
    }

    @Test
    public void formatElapsedTimeSingleDigitSeconds() {
        GameState state = new GameState();
        state.setElapsedSeconds( 63 );
        assertEquals( "1:03", state.formatElapsedTime() );
    }

    @Test
    public void formatElapsedTimeLargeDuration() {
        GameState state = new GameState();
        state.setElapsedSeconds( 3661 );
        assertEquals( "61:01", state.formatElapsedTime() );
    }
}
