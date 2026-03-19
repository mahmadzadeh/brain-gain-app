package com.spatialplanning.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SpatialTree {

    private static final Map<SlotId, SlotId[]> BLOCKERS = buildBlockers();

    private final Map<SlotId, Integer> placements;

    private SpatialTree(Map<SlotId, Integer> placements) {
        this.placements = placements;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean canMove(SlotId slotId) {
        if (!placements.containsKey(slotId)) {
            return false;
        }
        for (SlotId blocker : BLOCKERS.get(slotId)) {
            if (placements.containsKey(blocker)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasBallAt(SlotId slotId) {
        return placements.containsKey(slotId);
    }

    public int ballAt(SlotId slotId) {
        if (!placements.containsKey(slotId)) {
            throw new IllegalStateException("No ball at " + slotId);
        }
        return placements.get(slotId);
    }

    List<SlotId[]> validMoves() {
        List<SlotId[]> moves = new ArrayList<>();
        for (SlotId from : SlotId.values()) {
            if (!canMove(from)) continue;
            for (SlotId to : SlotId.values()) {
                if (from == to) continue;
                if (!placements.containsKey(to) && !isBlockedDestination(to, from)) {
                    moves.add(new SlotId[]{from, to});
                }
            }
        }
        return moves;
    }

    private boolean isBlockedDestination(SlotId to, SlotId excludeFrom) {
        for (SlotId blocker : BLOCKERS.get(to)) {
            if (blocker != excludeFrom && placements.containsKey(blocker)) {
                return true;
            }
        }
        return false;
    }

    public SpatialTree move(SlotId from, SlotId to) {
        if (!placements.containsKey(from)) {
            throw new IllegalStateException("No ball at " + from);
        }
        if (!canMove(from)) {
            throw new IllegalStateException("Source is blocked: " + from);
        }
        if (placements.containsKey(to)) {
            throw new IllegalStateException("Destination occupied: " + to);
        }

        Map<SlotId, Integer> nextPlacements = new EnumMap<>(placements);
        int value = nextPlacements.remove(from);

        for (SlotId blocker : BLOCKERS.get(to)) {
            if (nextPlacements.containsKey(blocker)) {
                throw new IllegalStateException("Destination blocked: " + to);
            }
        }

        nextPlacements.put(to, value);
        return new SpatialTree(Collections.unmodifiableMap(nextPlacements));
    }

    private static Map<SlotId, SlotId[]> buildBlockers() {
        Map<SlotId, SlotId[]> blockers = new EnumMap<>(SlotId.class);
        blockers.put(SlotId.TOP_LEFT, new SlotId[]{});
        blockers.put(SlotId.TOP_RIGHT, new SlotId[]{});
        blockers.put(SlotId.MID_LEFT_OUTER, new SlotId[]{});
        blockers.put(SlotId.MID_LEFT_INNER, new SlotId[]{SlotId.MID_LEFT_OUTER});
        blockers.put(SlotId.MID_RIGHT_OUTER, new SlotId[]{});
        blockers.put(SlotId.MID_RIGHT_INNER, new SlotId[]{SlotId.MID_RIGHT_OUTER});
        blockers.put(SlotId.BOTTOM_LEFT_OUTER, new SlotId[]{});
        blockers.put(SlotId.BOTTOM_LEFT_MIDDLE, new SlotId[]{SlotId.BOTTOM_LEFT_OUTER});
        blockers.put(SlotId.BOTTOM_LEFT_INNER, new SlotId[]{SlotId.BOTTOM_LEFT_MIDDLE, SlotId.BOTTOM_LEFT_OUTER});
        blockers.put(SlotId.BOTTOM_RIGHT_OUTER, new SlotId[]{});
        blockers.put(SlotId.BOTTOM_RIGHT_MIDDLE, new SlotId[]{SlotId.BOTTOM_RIGHT_OUTER});
        blockers.put(SlotId.BOTTOM_RIGHT_INNER, new SlotId[]{SlotId.BOTTOM_RIGHT_MIDDLE, SlotId.BOTTOM_RIGHT_OUTER});
        return Collections.unmodifiableMap(blockers);
    }

    public static class Builder {

        private final Map<SlotId, Integer> placements = new EnumMap<>(SlotId.class);

        public Builder withPlacement(SlotId slotId, int value) {
            placements.put(slotId, value);
            return this;
        }

        public SpatialTree build() {
            return new SpatialTree(Collections.unmodifiableMap(new EnumMap<>(placements)));
        }
    }
}
