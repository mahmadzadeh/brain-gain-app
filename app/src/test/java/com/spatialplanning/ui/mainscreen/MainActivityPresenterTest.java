package com.spatialplanning.ui.mainscreen;

import com.spatialplanning.game.SlotId;
import com.spatialplanning.game.SpatialTree;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MainActivityPresenterTest {

    private MainScreenView view;
    private MainActivityPresenter presenter;

    @Before
    public void setUp() {
        view = mock(MainScreenView.class);
        presenter = new MainActivityPresenter(view, 3);
    }

    @Test
    public void displayCurrentStateShowsTreeAndMoveCount() {
        presenter.displayCurrentState();

        verify(view).displayTree(any(SpatialTree.class));
        verify(view).updateMoveCount(0);
        verify(view).updateLevel(3);
    }

    @Test
    public void tappingOccupiedUnblockedSlotSelectsIt() {
        // Find a slot that has a ball and is movable in the generated tree
        SpatialTree tree = presenter.currentTree();
        SlotId movableSlot = findMovableSlot(tree);

        presenter.onSlotTapped(movableSlot);

        verify(view).highlightSelectedBall(movableSlot);
    }

    @Test
    public void tappingEmptySlotAfterSelectionMakesMoveAndUpdatesView() {
        SpatialTree tree = presenter.currentTree();
        SlotId movableSlot = findMovableSlot(tree);
        SlotId emptySlot = findEmptyUnblockedSlot(tree, movableSlot);

        presenter.onSlotTapped(movableSlot);
        reset(view);
        presenter.onSlotTapped(emptySlot);

        verify(view).displayTree(any(SpatialTree.class));
        verify(view).updateMoveCount(1);
    }

    @Test
    public void tappingBlockedSlotDoesNotSelect() {
        SpatialTree tree = presenter.currentTree();
        SlotId blockedSlot = findBlockedSlot(tree);

        if (blockedSlot != null) {
            presenter.onSlotTapped(blockedSlot);
            verify(view, never()).highlightSelectedBall(any());
        }
    }

    @Test
    public void tappingEmptySlotWithNoSelectionDoesNothing() {
        SpatialTree tree = presenter.currentTree();
        SlotId emptySlot = findEmptySlot(tree);

        presenter.onSlotTapped(emptySlot);

        verify(view, never()).highlightSelectedBall(any());
        verify(view, never()).displayTree(any());
    }

    @Test
    public void tappingSelectedSlotAgainClearsSelection() {
        SpatialTree tree = presenter.currentTree();
        SlotId movableSlot = findMovableSlot(tree);

        presenter.onSlotTapped(movableSlot);
        reset(view);
        presenter.onSlotTapped(movableSlot);

        verify(view).clearSelection();
    }

    @Test
    public void tappingAnotherOccupiedSlotChangesSelection() {
        SpatialTree tree = presenter.currentTree();
        SlotId first = null;
        SlotId second = null;

        for (SlotId slot : SlotId.values()) {
            if (tree.canMove(slot)) {
                if (first == null) {
                    first = slot;
                } else {
                    second = slot;
                    break;
                }
            }
        }

        if (first != null && second != null) {
            presenter.onSlotTapped(first);
            reset(view);
            presenter.onSlotTapped(second);

            verify(view).highlightSelectedBall(second);
        }
    }

    @Test
    public void moveCountIncrementsWithEachValidMove() {
        SpatialTree tree = presenter.currentTree();
        SlotId movable = findMovableSlot(tree);
        SlotId empty = findEmptyUnblockedSlot(tree, movable);

        presenter.onSlotTapped(movable);
        presenter.onSlotTapped(empty);

        verify(view).updateMoveCount(1);
    }

    @Test
    public void tappingOccupiedSlotOnBlockedDestinationDoesNotMove() {
        SpatialTree tree = presenter.currentTree();
        SlotId movableSlot = findMovableSlot(tree);

        presenter.onSlotTapped(movableSlot);

        // Tap another occupied slot — should change selection, not move
        SlotId occupiedSlot = findOtherOccupiedSlot(tree, movableSlot);
        if (occupiedSlot != null) {
            reset(view);
            presenter.onSlotTapped(occupiedSlot);
            verify(view, never()).updateMoveCount(anyInt());
        }
    }

    // --- Helper methods ---

    private SlotId findMovableSlot(SpatialTree tree) {
        for (SlotId slot : SlotId.values()) {
            if (tree.canMove(slot)) {
                return slot;
            }
        }
        throw new IllegalStateException("No movable slot found");
    }

    private SlotId findEmptySlot(SpatialTree tree) {
        for (SlotId slot : SlotId.values()) {
            if (!tree.hasBallAt(slot)) {
                return slot;
            }
        }
        throw new IllegalStateException("No empty slot found");
    }

    private SlotId findEmptyUnblockedSlot(SpatialTree tree, SlotId source) {
        for (SlotId slot : SlotId.values()) {
            if (!tree.hasBallAt(slot)) {
                try {
                    tree.move(source, slot);
                    return slot;
                } catch (IllegalStateException ignored) {
                }
            }
        }
        throw new IllegalStateException("No valid destination found for " + source);
    }

    private SlotId findBlockedSlot(SpatialTree tree) {
        for (SlotId slot : SlotId.values()) {
            if (tree.hasBallAt(slot) && !tree.canMove(slot)) {
                return slot;
            }
        }
        return null;
    }

    private SlotId findOtherOccupiedSlot(SpatialTree tree, SlotId exclude) {
        for (SlotId slot : SlotId.values()) {
            if (slot != exclude && tree.hasBallAt(slot)) {
                return slot;
            }
        }
        return null;
    }
}
