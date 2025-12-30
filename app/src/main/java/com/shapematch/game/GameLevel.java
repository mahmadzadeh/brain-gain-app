package com.shapematch.game;

public class GameLevel {

    public static int MAX_LEVEL_SHAPE_COUNT = CellGrid.GRID_ROW_CNT * CellGrid.GRID_COL_CNT;

    public static GameLevel levelOne = new GameLevel(1);
    public static GameLevel initialLevel = levelOne;

    private int shapeCount;

    public GameLevel(int shapeCnt) {
        // Shape Match level is defined as the number of shapes shown and should never be 0.
        shapeCount = Math.max(1, shapeCnt);
    }

    public GameLevel nextLevel() {
        return hasReachedMaxLevel() ? this : new GameLevel(shapeCount + 1);
    }

    private boolean hasReachedMaxLevel() {
        return shapeCount == MAX_LEVEL_SHAPE_COUNT;
    }

    // for now the points for each level is equal to the
    // shape count presented in that level.
    public int points() {
        return getShapeCount();
    }

    public int getShapeCount() {
        return shapeCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameLevel gameLevel = (GameLevel) o;

        return getShapeCount() == gameLevel.getShapeCount();
    }

    @Override
    public int hashCode() {
        return getShapeCount();
    }
}
