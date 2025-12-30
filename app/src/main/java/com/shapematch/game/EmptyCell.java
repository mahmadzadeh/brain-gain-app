package com.shapematch.game;

import com.shapematch.data.shapes.NullShape;

public class EmptyCell extends Cell {

    public EmptyCell() {
        super(new NullShape());
    }
}
