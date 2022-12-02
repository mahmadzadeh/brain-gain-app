package com.monkeyladder.util.random;

import com.monkeyladder.util.IntegerRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class RandomNumberGenerator {

    private final static Random random = new Random( System.currentTimeMillis() );

    public static Integer next( IntegerRange range ) {
        return random.nextInt( range.upperBound() - range.lowerBound() + 1 ) + range.lowerBound();
    }

    public static List<Integer> next_N_DistinctRandomIntsWithinRangeInRandomOrder( int n, IntegerRange range ) {
        Set<Integer> integers = generate( n, range, new HashSet<>() );

        List<Integer> result = new ArrayList<>( integers );

        Collections.shuffle( result );

        enforceInvariant( n, result );

        return result;
    }

    private static void enforceInvariant( int n, List<Integer> result ) {
        if ( n != result.size() ) {
            throw new IllegalStateException( "Asked for N=" + n + " distinct random ints but got " + result.size() );
        }
    }

    // tail rec
    private static Set<Integer> generate( int n, IntegerRange range, Set<Integer> acc ) {
        if ( n == 0 )
            return acc;
        else {
            int sizeBefore = acc.size();
            acc.add( next( range ) );
            return generate( n - ( acc.size() - sizeBefore ), range, acc );
        }
    }
}

