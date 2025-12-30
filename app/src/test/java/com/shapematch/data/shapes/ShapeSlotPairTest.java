package com.shapematch.data.shapes;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ShapeSlotPairTest {

    @Test
    public void testTwoEqualShapeCellPair() {
        ShapeSlotPair onePair = new ShapeSlotPair(new HollowCircle(), 0);
        ShapeSlotPair anotherPair = new ShapeSlotPair(new HollowCircle(), 0);

        assertTrue(onePair.equals(anotherPair));
    }

    @Test
    public void testTwoUnequalShapeCellPair() {
        ShapeSlotPair onePair = new ShapeSlotPair(new HollowCircle(), 0);
        ShapeSlotPair anotherPair = new ShapeSlotPair(new FourSquare(), 0);

        assertFalse(onePair.equals(anotherPair));
    }

    @Test
    public void testTwoShapeCellPairsWithDifferentSlotsAreUnequal() {
        ShapeSlotPair onePair = new ShapeSlotPair(new HollowCircle(), 0);
        ShapeSlotPair anotherPair = new ShapeSlotPair(new HollowCircle(), 1);

        assertFalse(onePair.equals(anotherPair));
    }
}
