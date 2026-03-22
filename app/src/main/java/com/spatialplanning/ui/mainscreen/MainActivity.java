package com.spatialplanning.ui.mainscreen;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.monkeyladder.R;
import com.spatialplanning.game.SlotId;
import com.spatialplanning.game.SpatialTree;

public class MainActivity extends AppCompatActivity implements MainScreenView {

    private static final int INITIAL_COMPLEXITY_ROUND = 1;

    private MainActivityPresenter presenter;
    private SpatialTreeView treeView;
    private TextView timerText;
    private TextView moveCountText;
    private TextView levelText;
    private TextView scoreText;
    private TextView feedbackText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spatial_planning_main);

        timerText = findViewById(R.id.timerText);
        moveCountText = findViewById(R.id.moveCountText);
        levelText = findViewById(R.id.levelText);
        scoreText = findViewById(R.id.scoreText);
        feedbackText = findViewById(R.id.feedbackText);
        treeView = findViewById(R.id.spatialTreeView);

        presenter = new MainActivityPresenter(this, INITIAL_COMPLEXITY_ROUND);

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
    public void animateRoundStart(SpatialTree tree) {
        treeView.animateRoundStart(tree);
    }

    @Override
    public void updateMoveCount(int moves) {
        moveCountText.setText(getString(R.string.spatial_planning_moves_format, moves));
    }

    @Override
    public void updateLevel(int level) {
        levelText.setText(getString(R.string.spatial_planning_level_format, level));
    }

    @Override
    public void updateScore(int score) {
        scoreText.setText(getString(R.string.spatial_planning_score_format, score));
    }

    @Override
    public void setCountDownText(String text) {
        timerText.setText(text);
    }

    @Override
    public void showFeedback(boolean isCorrect) {
        feedbackText.setVisibility(android.view.View.VISIBLE);
        feedbackText.setText(isCorrect
                ? getString(R.string.spatial_planning_feedback_correct)
                : getString(R.string.spatial_planning_feedback_try_again));
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
