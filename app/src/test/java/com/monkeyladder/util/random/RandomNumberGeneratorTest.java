package com.monkeyladder.util.random;

import com.monkeyladder.util.IntegerRange;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RandomNumberGeneratorTest {

    @Test
    public void givenZeroDistinctInts_thenNext_N_DistinctRandomIntsWithinRangeInRandomOrder_returnsListOfSize_0( ) {

        List<Integer> list =
                RandomNumberGenerator.next_N_DistinctRandomIntsWithinRangeInRandomOrder( 0,
                        new IntegerRange( 0, 100 ) );

        assertEquals( 0, list.size() );
    }

    @Test
    public void givenOneDistinctInts_thenNext_N_DistinctRandomIntsWithinRangeInRandomOrder_returnsListOfSize_1( ) {

        IntegerRange range = new IntegerRange( 0, 100 );

        List<Integer> list =
                RandomNumberGenerator.next_N_DistinctRandomIntsWithinRangeInRandomOrder( 1,
                        range );

        assertEquals( 1, list.size() );
        assertValuesWithinRange( list, range );
    }

    @Test
    public void given10DistinctInts_thenNext_N_DistinctRandomIntsWithinRangeInRandomOrder_returnsListOfSize_1( ) {

        IntegerRange range = new IntegerRange( 0, 100 );
        int numOfInts = 10;
        List<Integer> list =
                RandomNumberGenerator.next_N_DistinctRandomIntsWithinRangeInRandomOrder( numOfInts, range );

        assertEquals( numOfInts, list.size() );
        assertValuesWithinRange( list, range );
    }

    @Test
    public void givenMultipleInvocations_thenNDistinctMethodBehavesCorrectly( ) {
        IntegerRange range = new IntegerRange( 0, 100 );
        int numOfInts = 10;
        List<Integer> list;

        for ( int i = 0; i < 10000; i++ ) {
            list = RandomNumberGenerator.next_N_DistinctRandomIntsWithinRangeInRandomOrder( numOfInts, range );

            assertEquals( numOfInts, list.size() );
            assertValuesWithinRange( list, range );
        }
    }

    private void assertValuesWithinRange( List<Integer> list, IntegerRange range ) {
        for ( Integer i : list ) {
            assertTrue( range.isInRange( i ) );
        }
    }

}