package com.shapematch.util;

import com.shapematch.util.random.RandomBoolean;

import org.junit.Test;

import static org.junit.Assert.*;

public class RandomBooleanTest {

    @Test
    public void nextRandomTrueWithOneOutOfNChanceReturnsBoolean() {
        Boolean result = RandomBoolean.nextRandomTrueWithOneOutOfNChance(2);

        assertNotNull(result);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void nextRandomTrueWithOneOutOfOneChanceAlwaysReturnsTrue() {
        for (int i = 0; i < 100; i++) {
            Boolean result = RandomBoolean.nextRandomTrueWithOneOutOfNChance(1);
            assertTrue(result);
        }
    }

    @Test
    public void nextRandomTrueWithOneOutOfTwoChanceReturnsVariedResults() {
        int trueCount = 0;
        int falseCount = 0;
        int iterations = 1000;

        for (int i = 0; i < iterations; i++) {
            if (RandomBoolean.nextRandomTrueWithOneOutOfNChance(2)) {
                trueCount++;
            } else {
                falseCount++;
            }
        }

        assertTrue(trueCount > 0);
        assertTrue(falseCount > 0);

        double ratio = (double) trueCount / iterations;
        assertTrue("Expected ~50% true results, got " + (ratio * 100) + "%",
                   ratio > 0.3 && ratio < 0.7);
    }

    @Test
    public void nextRandomTrueWithOneOutOfTenChanceReturnsRarelyTrue() {
        int trueCount = 0;
        int iterations = 1000;

        for (int i = 0; i < iterations; i++) {
            if (RandomBoolean.nextRandomTrueWithOneOutOfNChance(10)) {
                trueCount++;
            }
        }

        double ratio = (double) trueCount / iterations;
        assertTrue("Expected ~10% true results, got " + (ratio * 100) + "%",
                   ratio > 0.05 && ratio < 0.20);
    }
}
