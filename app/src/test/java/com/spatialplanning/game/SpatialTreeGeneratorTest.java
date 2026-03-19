package com.spatialplanning.game;

import org.junit.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SpatialTreeGeneratorTest {

    private static final Map<SlotId, Integer> SOLVED_PLACEMENTS = buildSolvedPlacements();

    private static Map<SlotId, Integer> buildSolvedPlacements() {
        Map<SlotId, Integer> m = new EnumMap<>(SlotId.class);
        m.put(SlotId.TOP_LEFT, 1);
        m.put(SlotId.TOP_RIGHT, 2);
        m.put(SlotId.MID_LEFT_OUTER, 3);
        m.put(SlotId.MID_LEFT_INNER, 4);
        m.put(SlotId.MID_RIGHT_INNER, 5);
        m.put(SlotId.MID_RIGHT_OUTER, 6);
        m.put(SlotId.BOTTOM_LEFT_OUTER, 7);
        m.put(SlotId.BOTTOM_LEFT_MIDDLE, 8);
        m.put(SlotId.BOTTOM_LEFT_INNER, 9);
        return m;
    }

    @Test
    public void solvedStateHasNineBallsInCorrectSlots() {
        SpatialTree solved = SpatialTreeGenerator.solvedState();

        for (Map.Entry<SlotId, Integer> entry : SOLVED_PLACEMENTS.entrySet()) {
            assertTrue(solved.hasBallAt(entry.getKey()));
            assertEquals((int) entry.getValue(), solved.ballAt(entry.getKey()));
        }

        assertFalse(solved.hasBallAt(SlotId.BOTTOM_RIGHT_INNER));
        assertFalse(solved.hasBallAt(SlotId.BOTTOM_RIGHT_MIDDLE));
        assertFalse(solved.hasBallAt(SlotId.BOTTOM_RIGHT_OUTER));
    }

    @Test
    public void generateReturnsTreeWithNineBalls() {
        SpatialTree tree = SpatialTreeGenerator.generate(5);
        int count = 0;
        for (SlotId slot : SlotId.values()) {
            if (tree.hasBallAt(slot)) {
                count++;
            }
        }
        assertEquals(9, count);
    }

    @Test
    public void generateWithZeroMovesReturnsSolvedState() {
        SpatialTree tree = SpatialTreeGenerator.generate(0);
        SpatialTree solved = SpatialTreeGenerator.solvedState();

        for (SlotId slot : SlotId.values()) {
            assertEquals(solved.hasBallAt(slot), tree.hasBallAt(slot));
            if (solved.hasBallAt(slot)) {
                assertEquals(solved.ballAt(slot), tree.ballAt(slot));
            }
        }
    }

    @Test
    public void generateWithMovesProducesValidState() {
        SpatialTree tree = SpatialTreeGenerator.generate(10);
        assertNotNull(tree);

        // every ball value 1-9 must still be present
        boolean[] found = new boolean[10];
        for (SlotId slot : SlotId.values()) {
            if (tree.hasBallAt(slot)) {
                int val = tree.ballAt(slot);
                assertTrue(val >= 1 && val <= 9);
                assertFalse("duplicate ball " + val, found[val]);
                found[val] = true;
            }
        }
        for (int i = 1; i <= 9; i++) {
            assertTrue("missing ball " + i, found[i]);
        }
    }

    @Test
    public void generateWithManyMovesHasAtLeastOneValidMove() {
        SpatialTree tree = SpatialTreeGenerator.generate(20);

        boolean hasValidMove = false;
        for (SlotId slot : SlotId.values()) {
            if (tree.canMove(slot)) {
                hasValidMove = true;
                break;
            }
        }
        assertTrue("scrambled tree should have at least one valid move", hasValidMove);
    }

    @Test
    public void generateWithHighScrambleDisplacesBalls() {
        SpatialTree tree = SpatialTreeGenerator.generate(20);
        SpatialTree solved = SpatialTreeGenerator.solvedState();

        int displaced = 0;
        for (Map.Entry<SlotId, Integer> entry : SOLVED_PLACEMENTS.entrySet()) {
            if (!tree.hasBallAt(entry.getKey()) ||
                    tree.ballAt(entry.getKey()) != entry.getValue()) {
                displaced++;
            }
        }
        assertTrue("expected some balls displaced, got " + displaced, displaced > 0);
    }
}
