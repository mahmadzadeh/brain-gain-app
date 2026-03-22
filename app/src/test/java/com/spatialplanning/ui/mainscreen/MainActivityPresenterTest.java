package com.spatialplanning.ui.mainscreen;

import com.spatialplanning.game.SpatialTree;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class MainActivityPresenterTest {

    private MainScreenView view;
    private MainActivityPresenter presenter;

    @Before
    public void setUp() {
        view = mock(MainScreenView.class);
        presenter = new MainActivityPresenter(view, 1);
    }

    @Test
    public void displayCurrentStateShowsInitialRoundAndScore() {
        presenter.displayCurrentState();

        verify(view).animateRoundStart(any(SpatialTree.class));
        verify(view).updateMoveCount(0);
        verify(view).updateLevel(2);
        verify(view).updateScore(0);
    }

    @Test
    public void solvingRoundDoesNotFinishWholeSession() {
        presenter.onRoundSolved();

        verify(view, never()).onFinish(anyInt(), anyInt());
        verify(view).updateScore(anyInt());
    }

    @Test
    public void solvingRoundStartsNewRoundWithHigherComplexity() {
        presenter.onRoundSolved();

        assertEquals(2, presenter.currentRound());
        assertEquals(3, presenter.currentRoundSolveMovesTarget());
        verify(view).updateLevel(3);
        verify(view).animateRoundStart(any(SpatialTree.class));
    }

    @Test
    public void solvingRoundAwardsPositivePoints() {
        presenter.onRoundSolved();
        assertTrue(presenter.currentScore() > 0);
    }
}
