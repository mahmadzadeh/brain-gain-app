package com.shapematch.util.random;

import com.shapematch.util.IntegerRange;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomNumberGenerator {

    private final static Random random = new Random(System.currentTimeMillis());

    public static Integer next(IntegerRange range ) {
        return random.nextInt(range.upperBound() - range.lowerBound() + 1) + range.lowerBound();
    }

    public static  List<Integer> next_N_NumbersWithinRange(Integer n, IntegerRange range) {
        List<Integer> randomNumbers = new ArrayList<>();

        for(int i = 0; i < n ; i++) {
            randomNumbers.add(next(range));
        }
        return randomNumbers;
    }

    // n distinct number in range [ 0, range.upperBound() )
    public static Set<Integer> next_N_DistinctNumbersWithinRange(Integer n, IntegerRange range) {

        if( range.upperBound() < n ) {
            throw new IllegalArgumentException("range upper bound " + range.upperBound() + " too small to allow " + n + " distinct integers to be selected");
        }

        Set<Integer> randNumbers = new HashSet<>();

        Integer nextRandom;
        for(int i = 0; i < n ; i++) {
            boolean alreadyInSet;
            do {
                nextRandom = next(range);
                alreadyInSet = randNumbers.contains(nextRandom);
            } while ( alreadyInSet );

            randNumbers.add(nextRandom);
        }

        return randNumbers;
    }
}
