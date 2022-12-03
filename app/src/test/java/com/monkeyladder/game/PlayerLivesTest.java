package com.monkeyladder.game;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerLivesTest {

    @Test
    public void fromLifeCountWithInvalidCount( ) {
        assertEquals( PlayerLives.Zero, PlayerLives.fromLifeCount( 0 ) );
    }
}