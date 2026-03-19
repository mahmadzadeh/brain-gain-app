package com.spatialplanning.game;

import java.util.List;
import java.util.Random;

public class SpatialTreeGenerator {

    private static final Random RANDOM = new Random();

    public static SpatialTree solvedState() {
        return SpatialTree.builder()
                .withPlacement(SlotId.TOP_LEFT, 1)
                .withPlacement(SlotId.TOP_RIGHT, 2)
                .withPlacement(SlotId.MID_LEFT_OUTER, 3)
                .withPlacement(SlotId.MID_LEFT_INNER, 4)
                .withPlacement(SlotId.MID_RIGHT_INNER, 5)
                .withPlacement(SlotId.MID_RIGHT_OUTER, 6)
                .withPlacement(SlotId.BOTTOM_LEFT_OUTER, 7)
                .withPlacement(SlotId.BOTTOM_LEFT_MIDDLE, 8)
                .withPlacement(SlotId.BOTTOM_LEFT_INNER, 9)
                .build();
    }

    public static SpatialTree generate(int scrambleMoves) {
        SpatialTree tree = solvedState();

        for (int i = 0; i < scrambleMoves; i++) {
            List<SlotId[]> moves = tree.validMoves();
            if (moves.isEmpty()) break;
            SlotId[] chosen = moves.get(RANDOM.nextInt(moves.size()));
            tree = tree.move(chosen[0], chosen[1]);
        }

        return tree;
    }
}
