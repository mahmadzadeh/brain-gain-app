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
        return generate(scrambleMoves, true);
    }

    private static SpatialTree generate(int scrambleMoves, boolean requireDisplacement) {
        SpatialTree solved = solvedState();
        SpatialTree bestTree = solved;
        int bestDisplaced = 0;

        // Retry to avoid trivially solved/near-solved outcomes.
        for (int attempt = 0; attempt < 30; attempt++) {
            SpatialTree tree = solved;
            for (int i = 0; i < scrambleMoves; i++) {
                List<SlotId[]> moves = tree.validMoves();
                if (moves.isEmpty()) break;
                SlotId[] chosen = moves.get(RANDOM.nextInt(moves.size()));
                tree = tree.move(chosen[0], chosen[1]);
            }

            int displaced = displacedCount(tree, solved);
            if (displaced > bestDisplaced) {
                bestDisplaced = displaced;
                bestTree = tree;
            }
            if (!requireDisplacement || displaced > 0) {
                return tree;
            }
        }

        return bestTree;
    }

    public static SpatialTree generateForComplexity(ComplexityLevel complexityLevel) {
        int scrambleMoves = Math.max(1, complexityLevel.solveMovesTarget() * 2);
        return generate(scrambleMoves, complexityLevel.solveMovesTarget() > 0);
    }

    private static int displacedCount(SpatialTree tree, SpatialTree solved) {
        int displaced = 0;
        for (SlotId slot : SlotId.values()) {
            if (solved.hasBallAt(slot) != tree.hasBallAt(slot)) {
                displaced++;
                continue;
            }
            if (solved.hasBallAt(slot) && solved.ballAt(slot) != tree.ballAt(slot)) {
                displaced++;
            }
        }
        return displaced;
    }
}
