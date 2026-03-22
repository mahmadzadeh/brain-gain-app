package com.spatialplanning.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

public class SpatialTree {

    private static final Map<SlotId, SlotId[]> BLOCKERS = buildBlockers();
    private static final SlotId[] PEG_TOP_LEFT = new SlotId[]{SlotId.TOP_LEFT};
    private static final SlotId[] PEG_TOP_RIGHT = new SlotId[]{SlotId.TOP_RIGHT};
    private static final SlotId[] PEG_MID_LEFT = new SlotId[]{SlotId.MID_LEFT_INNER, SlotId.MID_LEFT_OUTER};
    private static final SlotId[] PEG_MID_RIGHT = new SlotId[]{SlotId.MID_RIGHT_INNER, SlotId.MID_RIGHT_OUTER};
    private static final SlotId[] PEG_BOTTOM_LEFT = new SlotId[]{SlotId.BOTTOM_LEFT_INNER, SlotId.BOTTOM_LEFT_MIDDLE, SlotId.BOTTOM_LEFT_OUTER};
    private static final SlotId[] PEG_BOTTOM_RIGHT = new SlotId[]{SlotId.BOTTOM_RIGHT_INNER, SlotId.BOTTOM_RIGHT_MIDDLE, SlotId.BOTTOM_RIGHT_OUTER};
    private static final SlotId[][] PEGS = new SlotId[][]{
            PEG_TOP_LEFT,
            PEG_TOP_RIGHT,
            PEG_MID_LEFT,
            PEG_MID_RIGHT,
            PEG_BOTTOM_LEFT,
            PEG_BOTTOM_RIGHT
    };
    private static final Map<SlotId, SlotId[]> PEG_FOR_SLOT = buildPegLookup();

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
        for (SlotId blocker : blockersFor(slotId)) {
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
        Integer value = placements.get(slotId);
        if (value == null) {
            throw new IllegalStateException("No ball at " + slotId);
        }
        return value;
    }

    List<SlotId[]> validMoves() {
        List<SlotId[]> moves = new ArrayList<>();
        for (SlotId from : SlotId.values()) {
            if (!canMove(from)) continue;
            Integer value = placements.get(from);
            if (value == null) {
                continue;
            }
            for (SlotId to : SlotId.values()) {
                if (from == to) continue;
                if (isSamePeg(from, to)) continue;
                if (!placements.containsKey(to)) {
                    Map<SlotId, Integer> nextPlacements = new EnumMap<>(placements);
                    nextPlacements.remove(from);
                    if (isBlockedDestination(to, from, nextPlacements)) {
                        continue;
                    }
                    nextPlacements.put(to, value.intValue());
                    if (!hasContiguousPegOccupancy(nextPlacements)) {
                        continue;
                    }
                    moves.add(new SlotId[]{from, to});
                }
            }
        }
        return moves;
    }

    private boolean isBlockedDestination(SlotId to, SlotId excludeFrom, Map<SlotId, Integer> state) {
        for (SlotId blocker : blockersFor(to)) {
            if (blocker != excludeFrom && state.containsKey(blocker)) {
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
        if (isSamePeg(from, to)) {
            throw new IllegalStateException("Same-peg moves are not allowed: " + from + " -> " + to);
        }

        Map<SlotId, Integer> nextPlacements = new EnumMap<>(placements);
        Integer value = nextPlacements.remove(from);
        if (value == null) {
            throw new IllegalStateException("No ball at " + from);
        }

        if (isBlockedDestination(to, from, nextPlacements)) {
            throw new IllegalStateException("Destination blocked: " + to);
        }
        nextPlacements.put(to, value);

        if (!hasContiguousPegOccupancy(nextPlacements)) {
            throw new IllegalStateException("Move would create non-contiguous peg occupancy: " + from + " -> " + to);
        }

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

    private static Map<SlotId, SlotId[]> buildPegLookup() {
        Map<SlotId, SlotId[]> pegLookup = new EnumMap<>(SlotId.class);
        pegLookup.put(SlotId.TOP_LEFT, PEG_TOP_LEFT);
        pegLookup.put(SlotId.TOP_RIGHT, PEG_TOP_RIGHT);
        pegLookup.put(SlotId.MID_LEFT_INNER, PEG_MID_LEFT);
        pegLookup.put(SlotId.MID_LEFT_OUTER, PEG_MID_LEFT);
        pegLookup.put(SlotId.MID_RIGHT_INNER, PEG_MID_RIGHT);
        pegLookup.put(SlotId.MID_RIGHT_OUTER, PEG_MID_RIGHT);
        pegLookup.put(SlotId.BOTTOM_LEFT_INNER, PEG_BOTTOM_LEFT);
        pegLookup.put(SlotId.BOTTOM_LEFT_MIDDLE, PEG_BOTTOM_LEFT);
        pegLookup.put(SlotId.BOTTOM_LEFT_OUTER, PEG_BOTTOM_LEFT);
        pegLookup.put(SlotId.BOTTOM_RIGHT_INNER, PEG_BOTTOM_RIGHT);
        pegLookup.put(SlotId.BOTTOM_RIGHT_MIDDLE, PEG_BOTTOM_RIGHT);
        pegLookup.put(SlotId.BOTTOM_RIGHT_OUTER, PEG_BOTTOM_RIGHT);
        return Collections.unmodifiableMap(pegLookup);
    }

    private boolean isSamePeg(SlotId from, SlotId to) {
        return pegFor(from) == pegFor(to);
    }

    private static boolean isPegContiguous(SlotId[] peg, Map<SlotId, Integer> state) {
        boolean seenEmpty = false;
        for (SlotId slotId : peg) {
            if (!state.containsKey(slotId)) {
                seenEmpty = true;
                continue;
            }
            if (seenEmpty) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasContiguousPegOccupancy(Map<SlotId, Integer> state) {
        for (SlotId[] peg : PEGS) {
            if (!isPegContiguous(peg, state)) {
                return false;
            }
        }
        return true;
    }

    boolean hasContiguousPegOccupancy() {
        return hasContiguousPegOccupancy(placements);
    }

    private static SlotId[] blockersFor(SlotId slotId) {
        SlotId[] blockers = BLOCKERS.get(slotId);
        if (blockers == null) {
            throw new NoSuchElementException("No blockers configured for slot: " + slotId);
        }
        return blockers;
    }

    private static SlotId[] pegFor(SlotId slotId) {
        SlotId[] peg = PEG_FOR_SLOT.get(slotId);
        if (peg == null) {
            throw new NoSuchElementException("No peg configured for slot: " + slotId);
        }
        return peg;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpatialTree)) return false;
        SpatialTree that = (SpatialTree) o;
        return Objects.equals(placements, that.placements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placements);
    }
}
