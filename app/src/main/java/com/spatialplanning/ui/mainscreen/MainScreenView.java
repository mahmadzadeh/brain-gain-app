package com.spatialplanning.ui.mainscreen;

import com.spatialplanning.game.SlotId;
import com.spatialplanning.game.SpatialTree;

public interface MainScreenView {

    void displayTree(SpatialTree tree);

    void updateMoveCount(int moves);

    void updateLevel(int level);

    void setCountDownText(String text);

    void showFeedback(boolean isCorrect);

    void onFinish(int finalScore, int moves);

    void highlightSelectedBall(SlotId slotId);

    void clearSelection();
}
