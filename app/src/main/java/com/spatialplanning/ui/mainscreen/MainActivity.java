package com.spatialplanning.ui.mainscreen;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.monkeyladder.R;
import com.spatialplanning.game.SlotId;
import com.spatialplanning.game.SpatialTree;

public class MainActivity extends AppCompatActivity implements MainScreenView {

    private static final int DEFAULT_SCRAMBLE_MOVES = 3;

    private MainActivityPresenter presenter;
    private SpatialTreeView treeView;
    private TextView timerText;
    private TextView moveCountText;
    private TextView levelText;
    private TextView feedbackText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spatial_planning_main);

        timerText = findViewById(R.id.timerText);
        moveCountText = findViewById(R.id.moveCountText);
        levelText = findViewById(R.id.levelText);
        feedbackText = findViewById(R.id.feedbackText);
        treeView = findViewById(R.id.spatialTreeView);

        presenter = new MainActivityPresenter(this, DEFAULT_SCRAMBLE_MOVES);

        treeView.setOnSlotTappedListener(slotId -> presenter.onSlotTapped(slotId));

        presenter.displayCurrentState();
        presenter.startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.pauseTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.startTimer();
    }

    @Override
    public void displayTree(SpatialTree tree) {
        treeView.setTree(tree);
    }

    @Override
    public void updateMoveCount(int moves) {
        moveCountText.setText("Moves: " + moves);
    }

    @Override
    public void updateLevel(int level) {
        levelText.setText("Level: " + level);
    }

    @Override
    public void setCountDownText(String text) {
        timerText.setText(text);
    }

    @Override
    public void showFeedback(boolean isCorrect) {
        feedbackText.setVisibility(android.view.View.VISIBLE);
        feedbackText.setText(isCorrect ? "Correct!" : "Try again");
        feedbackText.postDelayed(() -> feedbackText.setVisibility(android.view.View.GONE), 1000);
    }

    @Override
    public void onFinish(int finalScore, int moves) {
        // TODO: navigate to ContinueActivity with results
        finish();
    }

    @Override
    public void highlightSelectedBall(SlotId slotId) {
        treeView.setSelectedSlot(slotId);
    }

    @Override
    public void clearSelection() {
        treeView.clearSelection();
    }
}
