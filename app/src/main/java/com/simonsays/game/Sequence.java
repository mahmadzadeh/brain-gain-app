package com.simonsays.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * An ordered sequence of tile positions (0-based indices on the grid).
 * Grows by one random position each round.
 */
public class Sequence {

    private final List<Integer> positions;
    private final int gridSize;
    private final Random random;

    public Sequence( int gridSize, Random random ) {
        this.positions = new ArrayList<>();
        this.gridSize = gridSize;
        this.random = random;
    }

    public void addRandomPosition() {
        positions.add( random.nextInt( gridSize ) );
    }

    public int getPosition( int index ) {
        return positions.get( index );
    }

    public int length() {
        return positions.size();
    }

    public List<Integer> getPositions() {
        return Collections.unmodifiableList( positions );
    }
}
