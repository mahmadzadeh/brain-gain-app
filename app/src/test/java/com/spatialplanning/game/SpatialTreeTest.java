package com.spatialplanning.game;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SpatialTreeTest {

    @Test
    public void outerSlotsAreAlwaysFree() {
        SpatialTree tree = SpatialTree.builder()
                .withPlacement(SlotId.BOTTOM_LEFT_OUTER, 7)
                .withPlacement(SlotId.TOP_RIGHT, 2)
                .build();

        assertTrue(tree.canMove(SlotId.BOTTOM_LEFT_OUTER));
        assertTrue(tree.canMove(SlotId.TOP_RIGHT));
    }

    @Test
    public void middleSlotBlockedByOuter() {
        SpatialTree tree = SpatialTree.builder()
                .withPlacement(SlotId.BOTTOM_LEFT_OUTER, 7)
                .withPlacement(SlotId.BOTTOM_LEFT_MIDDLE, 8)
                .build();

        assertFalse(tree.canMove(SlotId.BOTTOM_LEFT_MIDDLE));
    }

    @Test
    public void innerSlotBlockedByAllFurtherSlots() {
        SpatialTree tree = SpatialTree.builder()
                .withPlacement(SlotId.BOTTOM_LEFT_OUTER, 7)
                .withPlacement(SlotId.BOTTOM_LEFT_MIDDLE, 8)
                .withPlacement(SlotId.BOTTOM_LEFT_INNER, 9)
                .build();

        assertFalse(tree.canMove(SlotId.BOTTOM_LEFT_INNER));
    }

    @Test
    public void innerSlotBecomesMovableOnceCleared() {
        SpatialTree tree = SpatialTree.builder()
                .withPlacement(SlotId.BOTTOM_LEFT_INNER, 9)
                .build();

        assertTrue(tree.canMove(SlotId.BOTTOM_LEFT_INNER));
    }

    @Test
    public void rightBranchUsesSameRules() {
        SpatialTree tree = SpatialTree.builder()
                .withPlacement(SlotId.BOTTOM_RIGHT_OUTER, 6)
                .withPlacement(SlotId.BOTTOM_RIGHT_MIDDLE, 3)
                .withPlacement(SlotId.BOTTOM_RIGHT_INNER, 4)
                .build();

        assertFalse(tree.canMove(SlotId.BOTTOM_RIGHT_INNER));
        assertFalse(tree.canMove(SlotId.BOTTOM_RIGHT_MIDDLE));
    }

    @Test
    public void midBranchInnerBlockedByOuter() {
        SpatialTree tree = SpatialTree.builder()
                .withPlacement(SlotId.MID_LEFT_OUTER, 5)
                .withPlacement(SlotId.MID_LEFT_INNER, 4)
                .build();

        assertFalse(tree.canMove(SlotId.MID_LEFT_INNER));
    }

    @Test
    public void midBranchInnerMovesWhenOuterFree() {
        SpatialTree tree = SpatialTree.builder()
                .withPlacement(SlotId.MID_LEFT_INNER, 4)
                .build();

        assertTrue(tree.canMove(SlotId.MID_LEFT_INNER));
    }

    @Test
    public void movePlacesBallDeeperWhenPathClear() {
        SpatialTree tree = SpatialTree.builder()
                .withPlacement(SlotId.BOTTOM_LEFT_OUTER, 7)
                .build();

        tree = tree.move(SlotId.BOTTOM_LEFT_OUTER, SlotId.BOTTOM_LEFT_MIDDLE);

        assertFalse(tree.hasBallAt(SlotId.BOTTOM_LEFT_OUTER));
        assertTrue(tree.hasBallAt(SlotId.BOTTOM_LEFT_MIDDLE));
    }

    @Test(expected = IllegalStateException.class)
    public void moveFailsWhenSourceBlocked() {
        SpatialTree tree = SpatialTree.builder()
                .withPlacement(SlotId.BOTTOM_LEFT_OUTER, 7)
                .withPlacement(SlotId.BOTTOM_LEFT_MIDDLE, 8)
                .build();

        tree.move(SlotId.BOTTOM_LEFT_MIDDLE, SlotId.TOP_LEFT);
    }

    @Test(expected = IllegalStateException.class)
    public void moveFailsWhenSourceEmpty() {
        SpatialTree tree = SpatialTree.builder().build();

        tree.move(SlotId.TOP_LEFT, SlotId.MID_LEFT_OUTER);
    }

    @Test(expected = IllegalStateException.class)
    public void moveFailsWhenDestinationBlocked() {
        SpatialTree tree = SpatialTree.builder()
                .withPlacement(SlotId.TOP_LEFT, 1)
                .withPlacement(SlotId.BOTTOM_LEFT_OUTER, 7)
                .withPlacement(SlotId.BOTTOM_LEFT_MIDDLE, 8)
                .build();

        tree.move(SlotId.TOP_LEFT, SlotId.BOTTOM_LEFT_INNER);
    }

    @Test
    public void moveToAnyUnblockedEmptySlot() {
        SpatialTree tree = SpatialTree.builder()
                .withPlacement(SlotId.TOP_LEFT, 1)
                .build();

        tree = tree.move(SlotId.TOP_LEFT, SlotId.BOTTOM_LEFT_MIDDLE);

        assertFalse(tree.hasBallAt(SlotId.TOP_LEFT));
        assertTrue(tree.hasBallAt(SlotId.BOTTOM_LEFT_MIDDLE));
    }

    @Test
    public void moveToNonAdjacentUnblockedSlot() {
        SpatialTree tree = SpatialTree.builder()
                .withPlacement(SlotId.TOP_LEFT, 1)
                .build();

        tree = tree.move(SlotId.TOP_LEFT, SlotId.MID_LEFT_INNER);

        assertFalse(tree.hasBallAt(SlotId.TOP_LEFT));
        assertTrue(tree.hasBallAt(SlotId.MID_LEFT_INNER));
    }

    @Test
    public void canMoveAcrossBottomJunction() {
        SpatialTree tree = SpatialTree.builder()
                .withPlacement(SlotId.BOTTOM_LEFT_INNER, 4)
                .build();

        tree = tree.move(SlotId.BOTTOM_LEFT_INNER, SlotId.BOTTOM_RIGHT_INNER);

        assertFalse(tree.hasBallAt(SlotId.BOTTOM_LEFT_INNER));
        assertTrue(tree.hasBallAt(SlotId.BOTTOM_RIGHT_INNER));
    }

    @Test
    public void topSlotsActAsLeaves() {
        SpatialTree tree = SpatialTree.builder()
                .withPlacement(SlotId.TOP_LEFT, 1)
                .withPlacement(SlotId.TOP_RIGHT, 2)
                .build();

        assertTrue(tree.canMove(SlotId.TOP_LEFT));
        assertTrue(tree.canMove(SlotId.TOP_RIGHT));
    }
}
