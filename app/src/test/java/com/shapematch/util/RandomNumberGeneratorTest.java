package com.shapematch.util;

import com.shapematch.util.random.RandomNumberGenerator;

import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class RandomNumberGeneratorTest {

    @Test
    public void givenARangeThenNextWillReturnRandomNumberInRange() {
        IntegerRange range = new IntegerRange(0 ,  10);

        Integer next = RandomNumberGenerator.next(range);

        assertTrue(next instanceof Integer);

        assertTrue(range.lowerBound() <= next && next <= range.upperBound());
    }

    @Test
    public void givenRNGThenCallToGetNRandomReturnsNRandomNumbersInRange() {
        IntegerRange range = new IntegerRange(0 ,  10);

        int n = 10;
        List<Integer> randomNumbers = RandomNumberGenerator.next_N_NumbersWithinRange(n, range);

        assertEquals(n, randomNumbers.size());

        assertNumbersInRange(randomNumbers, range);
    }

    @Test
    public void givenRangeThenCallToNextNDistinctReturnNDistinctRandomNumbers() {
        IntegerRange range = new IntegerRange(0 ,  100);
        int n = 10;

        Set<Integer> randomNumbers = RandomNumberGenerator.next_N_DistinctNumbersWithinRange(n, range);

        assertNumbersInRange(randomNumbers, range);
    }

    @Test
    public void givenRangeThenCallToNextNDistinctReturnCorrectNumberOfRandomNumbers() {
        IntegerRange range = new IntegerRange(0 ,  36);
        int n = 36;

        Set<Integer> randomNumbers = RandomNumberGenerator.next_N_DistinctNumbersWithinRange(n, range);

        assertNumbersInRange(randomNumbers, range);
        assertEquals( n , randomNumbers.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenRangeTooSmallToSelectNDistinctNumbersThenThrowException() {
        IntegerRange range = new IntegerRange(0 ,  10);
        int n = 15;

        RandomNumberGenerator.next_N_DistinctNumbersWithinRange(n, range);
    }

    private void assertNumbersInRange(Collection<Integer> randomNumbers, IntegerRange range) {
        for(Integer num: randomNumbers ) {
            assertTrue(num >= range.lowerBound());
            assertTrue(num <= range.upperBound());
        }
    }
}
