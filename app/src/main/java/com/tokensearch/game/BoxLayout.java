package com.tokensearch.game;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Fixed 4x5 grid. For each round, randomly selects N cells from the grid.
 */
public class BoxLayout {

    static final int COLS = 4;
    static final int ROWS = 5;
    static final int TOTAL_CELLS = COLS * ROWS;

    private final Random random;

    public BoxLayout( Random random ) {
        this.random = random;
    }

    public int cols() {
        return COLS;
    }

    public int rows() {
        return ROWS;
    }

    public List<Box> generate( int count ) {
        List<Integer> indices = IntStream.range( 0, TOTAL_CELLS )
            .boxed()
            .collect( Collectors.toList() );
        Collections.shuffle( indices, random );

        int pick = Math.min( count, TOTAL_CELLS );
        return indices.stream()
            .limit( pick )
            .map( i -> new Box( i / COLS, i % COLS ) )
            .collect( Collectors.toList() );
    }
}
