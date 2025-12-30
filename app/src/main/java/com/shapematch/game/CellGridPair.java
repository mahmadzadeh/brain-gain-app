package com.shapematch.game;

public class CellGridPair {

    private final CellGrid left;
    private final CellGrid right;

    public CellGridPair(CellGrid left, CellGrid right) {
        this.left = left;
        this.right = right;
    }

    public CellGrid leftGrid() {
        return left;
    }

    public CellGrid rightGrid() {
        return right;
    }
}
