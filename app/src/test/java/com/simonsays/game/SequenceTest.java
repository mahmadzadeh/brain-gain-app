package com.simonsays.game;

import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SequenceTest {

    @Test
    public void newSequenceIsEmpty() {
        Sequence sequence = new Sequence( 16, new Random() );
        assertEquals( 0, sequence.length() );
    }

    @Test
    public void addRandomPositionIncreasesLength() {
        Sequence sequence = new Sequence( 16, new Random() );
        sequence.addRandomPosition();
        assertEquals( 1, sequence.length() );

        sequence.addRandomPosition();
        assertEquals( 2, sequence.length() );
    }

    @Test
    public void getPositionReturnsCorrectValue() {
        Random seededRandom = new Random( 42 );
        Sequence sequence = new Sequence( 16, seededRandom );
        sequence.addRandomPosition();
        sequence.addRandomPosition();

        // Verify positions are within grid bounds
        assertTrue( sequence.getPosition( 0 ) >= 0 && sequence.getPosition( 0 ) < 16 );
        assertTrue( sequence.getPosition( 1 ) >= 0 && sequence.getPosition( 1 ) < 16 );
    }

    @Test
    public void positionsAreWithinGridBounds() {
        Sequence sequence = new Sequence( 4, new Random() );
        for ( int i = 0; i < 100; i++ ) {
            sequence.addRandomPosition();
            int pos = sequence.getPosition( i );
            assertTrue( "Position " + pos + " out of bounds", pos >= 0 && pos < 4 );
        }
    }

    @Test
    public void getPositionsReturnsUnmodifiableList() {
        Sequence sequence = new Sequence( 16, new Random() );
        sequence.addRandomPosition();
        List<Integer> positions = sequence.getPositions();

        try {
            positions.add( 5 );
            throw new AssertionError( "Expected UnsupportedOperationException" );
        } catch ( UnsupportedOperationException ignored ) {
            // expected
        }
    }

    @Test
    public void getPositionsReturnsAllAddedPositions() {
        Random seededRandom = new Random( 99 );
        Sequence sequence = new Sequence( 16, seededRandom );
        sequence.addRandomPosition();
        sequence.addRandomPosition();
        sequence.addRandomPosition();

        List<Integer> positions = sequence.getPositions();
        assertEquals( 3, positions.size() );
    }

    @Test( expected = IndexOutOfBoundsException.class )
    public void getPositionThrowsForInvalidIndex() {
        Sequence sequence = new Sequence( 16, new Random() );
        sequence.getPosition( 0 );
    }

    @Test
    public void deterministicWithSeededRandom() {
        Sequence s1 = new Sequence( 16, new Random( 123 ) );
        Sequence s2 = new Sequence( 16, new Random( 123 ) );

        for ( int i = 0; i < 10; i++ ) {
            s1.addRandomPosition();
            s2.addRandomPosition();
        }

        assertEquals( s1.getPositions(), s2.getPositions() );
    }
}
