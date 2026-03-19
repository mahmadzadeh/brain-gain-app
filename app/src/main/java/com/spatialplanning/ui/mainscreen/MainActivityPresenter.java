package com.spatialplanning.ui.mainscreen;

import android.os.CountDownTimer;
import android.util.Log;

import com.spatialplanning.game.SlotId;
import com.spatialplanning.game.SpatialTree;
import com.spatialplanning.game.SpatialTreeGenerator;

import java.util.Locale;

public class MainActivityPresenter implements MainViewContract.Presenter {

    private static final String TAG = "SpatialPlanningPresenter";
    private static final long GAME_DURATION_MILLIS = 90000;
    private static final long COUNTDOWN_INTERVAL_MILLIS = 1000;

    private final MainScreenView view;
    private final int scrambleMoves;

    private SpatialTree tree;
    private SlotId selectedSlot;
    private int moveCount;
    private CountDownTimer timer;
    private long remainingMillis = GAME_DURATION_MILLIS;

    public MainActivityPresenter(MainScreenView view, int scrambleMoves) {
        this.view = view;
        this.scrambleMoves = scrambleMoves;
        this.tree = SpatialTreeGenerator.generate(scrambleMoves);
        this.moveCount = 0;
    }

    public void displayCurrentState() {
        view.displayTree(tree);
        view.updateMoveCount(moveCount);
        view.updateLevel(scrambleMoves);
    }

    @Override
    public void onSlotTapped(SlotId slotId) {
        if (selectedSlot == null) {
            trySelect(slotId);
        } else if (selectedSlot == slotId) {
            clearSelection();
        } else if (tree.hasBallAt(slotId)) {
            // Tapped another occupied slot — change selection if movable
            if (tree.canMove(slotId)) {
                selectedSlot = slotId;
                view.highlightSelectedBall(slotId);
            }
        } else {
            tryMove(slotId);
        }
    }

    private void trySelect(SlotId slotId) {
        if (tree.hasBallAt(slotId) && tree.canMove(slotId)) {
            selectedSlot = slotId;
            view.highlightSelectedBall(slotId);
        }
    }

    private void tryMove(SlotId destination) {
        try {
            tree = tree.move(selectedSlot, destination);
            moveCount++;
            selectedSlot = null;

            view.displayTree(tree);
            view.updateMoveCount(moveCount);

            if (isSolved()) {
                Log.d(TAG, "Puzzle solved in " + moveCount + " moves");
                onFinish();
            }
        } catch (IllegalStateException e) {
            Log.d(TAG, "Invalid move: " + e.getMessage());
            clearSelection();
        }
    }

    private void clearSelection() {
        selectedSlot = null;
        view.clearSelection();
    }

    private boolean isSolved() {
        SpatialTree solved = SpatialTreeGenerator.solvedState();
        for (SlotId slot : SlotId.values()) {
            if (solved.hasBallAt(slot) != tree.hasBallAt(slot)) return false;
            if (solved.hasBallAt(slot) && solved.ballAt(slot) != tree.ballAt(slot)) return false;
        }
        return true;
    }

    @Override
    public void startTimer() {
        Log.d(TAG, "Timer started with " + remainingMillis + "ms remaining");

        if (timer != null) {
            timer.cancel();
        }

        timer = new CountDownTimer(remainingMillis, COUNTDOWN_INTERVAL_MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingMillis = millisUntilFinished;
                view.setCountDownText(formatTime(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                MainActivityPresenter.this.onFinish();
            }
        };

        timer.start();
    }

    @Override
    public void pauseTimer() {
        Log.d(TAG, "Timer paused at " + remainingMillis + "ms");
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onFinish() {
        Log.d(TAG, "Game finished. Moves: " + moveCount);
        if (timer != null) {
            timer.cancel();
        }
        view.onFinish(moveCount, moveCount);
    }

    SpatialTree currentTree() {
        return tree;
    }

    private String formatTime(long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.US, "%d:%02d", minutes, seconds);
    }
}
